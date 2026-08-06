package com.example.gigmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gigmarket.ui.screens.OpeningScreen
import com.example.gigmarket.ui.screens.SplashScreen
import com.example.gigmarket.ui.screens.UserLogin
import com.example.gigmarket.ui.screens.UserLoginForm
import com.example.gigmarket.ui.screens.UserDashboard
import com.example.gigmarket.ui.screens.WorkerProfileScreen
import com.example.gigmarket.ui.screens.WorkerLoginScreen
import com.example.gigmarket.ui.screens.WorkerSignupScreen
import com.example.gigmarket.ui.screens.WorkerHomeScreen
import com.example.gigmarket.ui.screens.WorkerLanguageScreen
import com.example.gigmarket.ui.screens.WorkerEarningsScreen
import com.example.gigmarket.ui.screens.WorkerRequestsScreen
import com.example.gigmarket.ui.screens.WorkerPendingWorksScreen
import com.example.gigmarket.ui.screens.WorkerSettingsScreen
import com.example.gigmarket.ui.screens.WorkerReviewHistoryScreen
import com.example.gigmarket.ui.screens.WorkerPrivacyScreen
import com.example.gigmarket.ui.screens.WorkerPersonalizationScreen
import com.example.gigmarket.ui.screens.WorkerUpdateScreen
import com.example.gigmarket.ui.theme.DarkBackground
import com.example.gigmarket.ui.theme.GigMarketTheme
import com.example.gigmarket.ui.theme.LightBackground
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gigmarket.viewmodel.LanguageViewModel
import com.example.gigmarket.viewmodel.ThemeViewModel
import com.example.gigmarket.ui.screens.SettingsScreen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            val languageViewModel: LanguageViewModel = viewModel()
            
            GigMarketTheme(darkTheme = isDarkTheme) {
                val surfaceColor = if (isDarkTheme) DarkBackground else LightBackground
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = surfaceColor
                ) {
                    val navController = rememberNavController()
                    
                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        // 1️⃣ Splash Screen
                        composable("splash") {
                            SplashScreen(navController = navController)
                        }
                        // 2️⃣ Landing Page
                        composable("/") {
                            OpeningScreen(navController = navController)
                        }
                        // 3️⃣ User Login (with form)
                        composable("/user-login-form") {
                            UserLoginForm(navController = navController, languageViewModel = languageViewModel)
                        }
                        // Old User Login (categories/map) - kept for existing flow
                        composable("/user-login") {
                            UserLogin(navController = navController, languageViewModel = languageViewModel)
                        }
                        // Worker Profile Screen
                        composable(
                            route = "/worker-profile/{workerId}/{workerName}/{workerPhone}/{workerCategory}/{workerRating}/{workerTotalReviews}/{workerBio}/{workerSkills}/{workerLanguages}/{workerHourlyRate}/{workerExperience}/{workerIsVerified}/{workerIsAvailable}/{workerLocationAddress}/{workerLocationLat}/{workerLocationLng}",
                            arguments = listOf(
                                navArgument("workerId") { type = NavType.StringType; defaultValue = "" },
                                navArgument("workerName") { type = NavType.StringType; defaultValue = "" },
                                navArgument("workerPhone") { type = NavType.StringType; defaultValue = "" },
                                navArgument("workerCategory") { type = NavType.StringType; defaultValue = "" },
                                navArgument("workerRating") { type = NavType.StringType; defaultValue = "0" },
                                navArgument("workerTotalReviews") { type = NavType.StringType; defaultValue = "0" },
                                navArgument("workerBio") { type = NavType.StringType; defaultValue = "" },
                                navArgument("workerSkills") { type = NavType.StringType; defaultValue = "" },
                                navArgument("workerLanguages") { type = NavType.StringType; defaultValue = "" },
                                navArgument("workerHourlyRate") { type = NavType.StringType; defaultValue = "0" },
                                navArgument("workerExperience") { type = NavType.StringType; defaultValue = "" },
                                navArgument("workerIsVerified") { type = NavType.StringType; defaultValue = "false" },
                                navArgument("workerIsAvailable") { type = NavType.StringType; defaultValue = "false" },
                                navArgument("workerLocationAddress") { type = NavType.StringType; defaultValue = "" },
                                navArgument("workerLocationLat") { type = NavType.StringType; defaultValue = "0" },
                                navArgument("workerLocationLng") { type = NavType.StringType; defaultValue = "0" }
                            )
                        ) { backStackEntry ->
                            val workerId = backStackEntry.arguments?.getString("workerId") ?: ""
                            val workerName = backStackEntry.arguments?.getString("workerName") ?: ""
                            val workerPhone = backStackEntry.arguments?.getString("workerPhone") ?: ""
                            val workerCategory = backStackEntry.arguments?.getString("workerCategory") ?: ""
                            val workerRating = backStackEntry.arguments?.getString("workerRating")?.toDoubleOrNull() ?: 0.0
                            val workerTotalReviews = backStackEntry.arguments?.getString("workerTotalReviews")?.toIntOrNull() ?: 0
                            val workerBio = backStackEntry.arguments?.getString("workerBio") ?: ""
                            val workerSkills = backStackEntry.arguments?.getString("workerSkills")?.split("|||") ?: emptyList()
                            val workerLanguages = backStackEntry.arguments?.getString("workerLanguages")?.split("|||") ?: emptyList()
                            val workerHourlyRate = backStackEntry.arguments?.getString("workerHourlyRate")?.toDoubleOrNull()
                            val workerExperience = backStackEntry.arguments?.getString("workerExperience") ?: ""
                            val workerIsVerified = backStackEntry.arguments?.getString("workerIsVerified")?.toBooleanStrictOrNull() ?: false
                            val workerIsAvailable = backStackEntry.arguments?.getString("workerIsAvailable")?.toBooleanStrictOrNull() ?: false
                            val workerLocationAddress = backStackEntry.arguments?.getString("workerLocationAddress") ?: ""
                            val workerLocationLat = backStackEntry.arguments?.getString("workerLocationLat")?.toDoubleOrNull() ?: 0.0
                            val workerLocationLng = backStackEntry.arguments?.getString("workerLocationLng")?.toDoubleOrNull() ?: 0.0
                            WorkerProfileScreen(
                                navController = navController,
                                workerId = workerId,
                                workerName = workerName,
                                workerPhone = workerPhone,
                                workerCategory = workerCategory,
                                workerRating = workerRating,
                                workerTotalReviews = workerTotalReviews,
                                workerBio = workerBio,
                                workerSkills = workerSkills,
                                workerLanguages = workerLanguages,
                                workerHourlyRate = workerHourlyRate,
                                workerExperience = workerExperience,
                                workerIsVerified = workerIsVerified,
                                workerIsAvailable = workerIsAvailable,
                                workerLocationAddress = workerLocationAddress,
                                workerLocationLat = workerLocationLat,
                                workerLocationLng = workerLocationLng
                            )
                        }
                        composable(
                            route = "/user-dashboard/{userName}",
                            arguments = listOf(
                                navArgument("userName") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )
                        ) { backStackEntry ->
                            val userName = backStackEntry.arguments?.getString("userName") ?: ""
                            UserDashboard(navController = navController, userName = userName, languageViewModel = languageViewModel)
                        }
                        // 4️⃣ Settings Screen
                        composable("/settings") {
                            SettingsScreen(
                                navController = navController, 
                                languageViewModel = languageViewModel,
                                themeViewModel = themeViewModel
                            )
                        }
                        composable("/provider-login") {
                            WorkerLoginScreen(navController = navController)
                        }
                        composable(
                            route = "worker_login",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerLoginScreen(navController = navController)
                        }
                        composable(
                            route = "worker_signup",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerSignupScreen(navController = navController)
                        }
                        composable(
                            route = "worker_home",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerHomeScreen(navController = navController)
                        }
                        composable(
                            route = "worker_language",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerLanguageScreen(navController = navController)
                        }
                        composable(
                            route = "worker_earnings",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerEarningsScreen(navController = navController)
                        }
                        composable(
                            route = "worker_requests",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerRequestsScreen(navController = navController)
                        }
                        composable(
                            route = "worker_pending_works",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerPendingWorksScreen(navController = navController)
                        }
                        composable(
                            route = "worker_settings",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerSettingsScreen(navController = navController)
                        }
                        composable(
                            route = "worker_review_history",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerReviewHistoryScreen(navController = navController)
                        }
                        composable(
                            route = "worker_profile",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerProfileScreen(navController = navController)
                        }
                        composable(
                            route = "worker_privacy",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerPrivacyScreen(navController = navController)
                        }
                        composable(
                            route = "worker_personalization",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerPersonalizationScreen(navController = navController)
                        }
                        composable(
                            route = "worker_update",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerUpdateScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

