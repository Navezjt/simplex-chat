package chat.simplex.app.views.chat.item

import android.content.*
import android.net.Uri
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chat.simplex.app.*
import chat.simplex.app.R
import chat.simplex.app.model.*
import chat.simplex.app.ui.theme.SimpleXTheme
import chat.simplex.app.views.chat.ComposeContextItem
import chat.simplex.app.views.chat.ComposeState
import chat.simplex.app.views.helpers.*
import kotlinx.datetime.Clock

@Composable
fun ChatItemView(
  user: User,
  cInfo: ChatInfo,
  cItem: ChatItem,
  composeState: MutableState<ComposeState>,
  cxt: Context,
  uriHandler: UriHandler? = null,
  imageProvider: (() -> ImageGalleryProvider)? = null,
  showMember: Boolean = false,
  chatModelIncognito: Boolean,
  useLinkPreviews: Boolean,
  deleteMessage: (Long, CIDeleteMode) -> Unit,
  receiveFile: (Long) -> Unit,
  joinGroup: (Long) -> Unit,
  acceptCall: (Contact) -> Unit,
  scrollToItem: (Long) -> Unit,
) {
  val context = LocalContext.current
  val sent = cItem.chatDir.sent
  val alignment = if (sent) Alignment.CenterEnd else Alignment.CenterStart
  val showMenu = remember { mutableStateOf(false) }
  val saveFileLauncher = rememberSaveFileLauncher(cxt = context, ciFile = cItem.file)
  Box(
    modifier = Modifier
      .padding(bottom = 4.dp)
      .fillMaxWidth(),
    contentAlignment = alignment,
  ) {
    Column(
      Modifier
        .clip(RoundedCornerShape(18.dp))
        .combinedClickable(onLongClick = { showMenu.value = true }, onClick = {})
    ) {
      @Composable fun ContentItem() {
        if (cItem.file == null && cItem.quotedItem == null && isShortEmoji(cItem.content.text)) {
          EmojiItemView(cItem)
        } else {
          val onLinkLongClick = { _: String -> showMenu.value = true }
          FramedItemView(cInfo, cItem, uriHandler, imageProvider, showMember = showMember, showMenu, receiveFile, onLinkLongClick, scrollToItem)
        }
        DropdownMenu(
          expanded = showMenu.value,
          onDismissRequest = { showMenu.value = false },
          Modifier.width(220.dp)
        ) {
          ItemAction(stringResource(R.string.reply_verb), Icons.Outlined.Reply, onClick = {
            if (composeState.value.editing) {
              composeState.value = ComposeState(contextItem = ComposeContextItem.QuotedItem(cItem), useLinkPreviews = useLinkPreviews)
            } else {
              composeState.value = composeState.value.copy(contextItem = ComposeContextItem.QuotedItem(cItem))
            }
            showMenu.value = false
          })
          ItemAction(stringResource(R.string.share_verb), Icons.Outlined.Share, onClick = {
            val filePath = getLoadedFilePath(SimplexApp.context, cItem.file)
            when {
              filePath != null -> shareFile(cxt, cItem.text, filePath)
              else -> shareText(cxt, cItem.content.text)
            }
            showMenu.value = false
          })
          ItemAction(stringResource(R.string.copy_verb), Icons.Outlined.ContentCopy, onClick = {
            copyText(cxt, cItem.content.text)
            showMenu.value = false
          })
          if (cItem.content.msgContent is MsgContent.MCImage || cItem.content.msgContent is MsgContent.MCFile) {
            val filePath = getLoadedFilePath(context, cItem.file)
            if (filePath != null) {
              ItemAction(stringResource(R.string.save_verb), Icons.Outlined.SaveAlt, onClick = {
                when (cItem.content.msgContent) {
                  is MsgContent.MCImage -> saveImage(context, cItem.file)
                  is MsgContent.MCFile -> saveFileLauncher.launch(cItem.file?.fileName)
                  else -> {}
                }
                showMenu.value = false
              })
            }
          }
          if (cItem.meta.editable) {
            ItemAction(stringResource(R.string.edit_verb), Icons.Filled.Edit, onClick = {
              composeState.value = ComposeState(editingItem = cItem, useLinkPreviews = useLinkPreviews)
              showMenu.value = false
            })
          }
          ItemAction(
            stringResource(R.string.delete_verb),
            Icons.Outlined.Delete,
            onClick = {
              showMenu.value = false
              deleteMessageAlertDialog(cItem, deleteMessage = deleteMessage)
            },
            color = Color.Red
          )
        }
      }

      @Composable fun DeletedItem() {
        DeletedItemView(cItem, showMember = showMember)
        DropdownMenu(
          expanded = showMenu.value,
          onDismissRequest = { showMenu.value = false },
          Modifier.width(220.dp)
        ) {
          ItemAction(
            stringResource(R.string.delete_verb),
            Icons.Outlined.Delete,
            onClick = {
              showMenu.value = false
              deleteMessageAlertDialog(cItem, deleteMessage = deleteMessage)
            },
            color = Color.Red
          )
        }
      }

      @Composable fun CallItem(status: CICallStatus, duration: Int) {
        CICallItemView(cInfo, cItem, status, duration, acceptCall)
      }

      when (val c = cItem.content) {
        is CIContent.SndMsgContent -> ContentItem()
        is CIContent.RcvMsgContent -> ContentItem()
        is CIContent.SndDeleted -> DeletedItem()
        is CIContent.RcvDeleted -> DeletedItem()
        is CIContent.SndCall -> CallItem(c.status, c.duration)
        is CIContent.RcvCall -> CallItem(c.status, c.duration)
        is CIContent.RcvIntegrityError -> IntegrityErrorItemView(cItem, showMember = showMember)
        is CIContent.RcvGroupInvitation -> CIGroupInvitationView(cItem, c.groupInvitation, c.memberRole, joinGroup = joinGroup, chatIncognito = cInfo.incognito)
        is CIContent.SndGroupInvitation -> CIGroupInvitationView(cItem, c.groupInvitation, c.memberRole, joinGroup = joinGroup, chatIncognito = cInfo.incognito)
        is CIContent.RcvGroupEventContent -> CIGroupEventView(cItem)
        is CIContent.SndGroupEventContent -> CIGroupEventView(cItem)
      }
    }
  }
}

@Composable
fun ItemAction(text: String, icon: ImageVector, onClick: () -> Unit, color: Color = MaterialTheme.colors.onBackground) {
  DropdownMenuItem(onClick) {
    Row {
      Text(
        text,
        modifier = Modifier
          .fillMaxWidth()
          .weight(1F)
          .padding(end = 15.dp),
        color = color
      )
      Icon(icon, text, tint = color)
    }
  }
}

fun deleteMessageAlertDialog(chatItem: ChatItem, deleteMessage: (Long, CIDeleteMode) -> Unit) {
  AlertManager.shared.showAlertDialogButtons(
    title = generalGetString(R.string.delete_message__question),
    text = generalGetString(R.string.delete_message_cannot_be_undone_warning),
    buttons = {
      Row(
        Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.End,
      ) {
        TextButton(onClick = {
          deleteMessage(chatItem.id, CIDeleteMode.cidmInternal)
          AlertManager.shared.hideAlert()
        }) { Text(stringResource(R.string.for_me_only)) }
        if (chatItem.meta.editable) {
          Spacer(Modifier.padding(horizontal = 4.dp))
          TextButton(onClick = {
            deleteMessage(chatItem.id, CIDeleteMode.cidmBroadcast)
            AlertManager.shared.hideAlert()
          }) { Text(stringResource(R.string.for_everybody)) }
        }
      }
    }
  )
}

@Preview
@Composable
fun PreviewChatItemView() {
  SimpleXTheme {
    ChatItemView(
      User.sampleData,
      ChatInfo.Direct.sampleData,
      ChatItem.getSampleData(
        1, CIDirection.DirectSnd(), Clock.System.now(), "hello"
      ),
      useLinkPreviews = true,
      composeState = remember { mutableStateOf(ComposeState(useLinkPreviews = true)) },
      cxt = LocalContext.current,
      chatModelIncognito = false,
      deleteMessage = { _, _ -> },
      receiveFile = {},
      joinGroup = {},
      acceptCall = { _ -> },
      scrollToItem = {},
    )
  }
}

@Preview
@Composable
fun PreviewChatItemViewDeletedContent() {
  SimpleXTheme {
    ChatItemView(
      User.sampleData,
      ChatInfo.Direct.sampleData,
      ChatItem.getDeletedContentSampleData(),
      useLinkPreviews = true,
      composeState = remember { mutableStateOf(ComposeState(useLinkPreviews = true)) },
      cxt = LocalContext.current,
      chatModelIncognito = false,
      deleteMessage = { _, _ -> },
      receiveFile = {},
      joinGroup = {},
      acceptCall = { _ -> },
      scrollToItem = {},
    )
  }
}
