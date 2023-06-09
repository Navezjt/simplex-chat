{-# LANGUAGE FlexibleContexts #-}

module Simplex.Chat.Mobile.WebRTC (
  cChatEncryptMedia,
  cChatDecryptMedia,
  chatEncryptMedia,
  chatDecryptMedia,
  reservedSize,
) where

import Control.Monad.Except
import qualified Crypto.Cipher.Types as AES
import Data.Bifunctor (bimap)
import qualified Data.ByteArray as BA
import qualified Data.ByteString as B
import qualified Data.ByteString.Base64.URL as U
import Data.ByteString.Internal (ByteString (PS), memcpy)
import Data.Either (fromLeft)
import Data.Word (Word8)
import Foreign.C (CInt, CString, newCAString)
import Foreign.ForeignPtr (newForeignPtr_)
import Foreign.ForeignPtr.Unsafe (unsafeForeignPtrToPtr)
import Foreign.Ptr (Ptr, plusPtr)
import qualified Simplex.Messaging.Crypto as C

cChatEncryptMedia :: CString -> Ptr Word8 -> CInt -> IO CString
cChatEncryptMedia = cTransformMedia chatEncryptMedia

cChatDecryptMedia :: CString -> Ptr Word8 -> CInt -> IO CString
cChatDecryptMedia = cTransformMedia chatDecryptMedia

cTransformMedia :: (ByteString -> ByteString -> ExceptT String IO ByteString) -> CString -> Ptr Word8 -> CInt -> IO CString
cTransformMedia f cKey cFrame cFrameLen = do
  key <- B.packCString cKey
  frame <- getFrame
  runExceptT (f key frame >>= liftIO . putFrame) >>= newCAString . fromLeft ""
  where
    getFrame = do
      fp <- newForeignPtr_ cFrame
      pure $ PS fp 0 $ fromIntegral cFrameLen
    putFrame bs@(PS fp offset _) = do
      let len = B.length bs
          p = unsafeForeignPtrToPtr fp `plusPtr` offset
      when (len <= fromIntegral cFrameLen) $ memcpy cFrame p len
{-# INLINE cTransformMedia #-}

chatEncryptMedia :: ByteString -> ByteString -> ExceptT String IO ByteString
chatEncryptMedia keyStr frame = do
  len <- checkFrameLen frame
  key <- decodeKey keyStr
  iv <- liftIO C.randomGCMIV
  (tag, frame') <- withExceptT show $ C.encryptAESNoPad key iv $ B.take len frame
  pure $ frame' <> BA.convert (C.unAuthTag tag) <> C.unGCMIV iv

chatDecryptMedia :: ByteString -> ByteString -> ExceptT String IO ByteString
chatDecryptMedia keyStr frame = do
  len <- checkFrameLen frame
  key <- decodeKey keyStr
  let (frame', rest) = B.splitAt len frame
      (tag, iv) = B.splitAt C.authTagSize rest
      authTag = C.AuthTag $ AES.AuthTag $ BA.convert tag
  withExceptT show $ do
    iv' <- liftEither $ C.gcmIV iv
    frame'' <- C.decryptAESNoPad key iv' frame' authTag
    pure $ frame'' <> framePad

checkFrameLen :: ByteString -> ExceptT String IO Int
checkFrameLen frame = do
  let len = B.length frame - reservedSize
  when (len < 0) $ throwError "frame has no [reserved space for] IV and/or auth tag"
  pure len
{-# INLINE checkFrameLen #-}

decodeKey :: ByteString -> ExceptT String IO C.Key
decodeKey = liftEither . bimap ("invalid key: " <>) C.Key . U.decode
{-# INLINE decodeKey #-}

reservedSize :: Int
reservedSize = C.authTagSize + C.gcmIVSize

framePad :: ByteString
framePad = B.replicate reservedSize 0
