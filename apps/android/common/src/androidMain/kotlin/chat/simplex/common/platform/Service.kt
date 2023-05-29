package chat.simplex.common.platform

import chat.simplex.common.model.ChatController
import chat.simplex.common.model.NotificationsMode
import chat.simplex.common.views.onboarding.OnboardingStage

actual suspend fun chatInitializedAndStarted() {
  // Prevents from showing "Enable notifications" alert when onboarding wasn't complete yet
  if (chatModel.onboardingStage.value == OnboardingStage.OnboardingComplete) {
    SimplexService.showBackgroundServiceNoticeIfNeeded()
    if (ChatController.appPrefs.notificationsMode.get() == NotificationsMode.SERVICE)
      serviceStart()
  }
}