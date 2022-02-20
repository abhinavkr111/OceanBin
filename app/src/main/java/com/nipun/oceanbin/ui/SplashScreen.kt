package com.nipun.oceanbin.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.nipun.oceanbin.R
import com.nipun.oceanbin.core.getAppSplashPath
import com.nipun.oceanbin.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Test() {
    Text(text = "Ok")
}

@Composable
fun SplashViewPager(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBg)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_viewpager_bg),
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.77f)
            ,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // This composable function is responsible for showing splash screen
            SetupViewPager(
                modifier = Modifier.fillMaxSize()
            ) {
                /*
                 * This lambda block will execute when user clicked continue.
                 * When all page of view pager is checked and user clicked continue we set
                 * a preference that our app is visited first time buy simply put a boolean value
                 * in splash screen.
                 */
                mainViewModel.setSplashViewed()
                navController.navigate(Screen.HomeScreen.route) {
                    popUpTo(Screen.SplashViewPager.route) {
                        inclusive = true
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SetupViewPager(
    modifier: Modifier = Modifier,
    onContinueClick: () -> Unit
) {

    // Details of view pager
    val pagerState = rememberPagerState(
        pageCount = 4,
        initialOffscreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0
    )

    // A global coroutine for performing async task in composable function
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${index + 1} page",
                    style = MaterialTheme.typography.h3
                )
                if (pagerState.currentPage == 3) {
                    Spacer(modifier = Modifier.size(MediumSpacing))
                    Button(onClick = { onContinueClick() }) {
                        Text(text = "Continue", style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
        /* If pager current page index is not equal to 3, i.e pager is not in last index
         * We can show skip button but when pager reached last index, if block will not execute
         * And skip button will be hide automatically.
         */
        if (pagerState.currentPage != 3) {
            Button(
                onClick =
                {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = 3
                        )
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.TopEnd)
            ) {
                Text(text = "SKIP")
            }
        }
        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        ) {
            if (pagerState.currentPage != 0) {
                Button(
                    onClick =
                    {
                        coroutineScope.launch {
                            if (!pagerState.isScrollInProgress) {
                                pagerState.scrollToPage(
                                    page = pagerState.currentPage - 1
                                )
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(text = "PREV")
                }
            }
            if (pagerState.currentPage != 3) {
                Button(
                    onClick =
                    {
                        coroutineScope.launch {
                            if (!pagerState.isScrollInProgress) {
                                pagerState.scrollToPage(
                                    page = pagerState.currentPage + 1
                                )
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text(text = "NEXT")
                }
            }

            // This composable function is responsible to show dot indicator
            Dots(
                dotCount = 4, currentSelect = pagerState.currentPage,
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun Dots(
    modifier: Modifier = Modifier,
    dotCount: Int,
    currentSelect: Int
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = scrollState) {
        scrollState.scrollTo(currentSelect)
    }
    Row(
        modifier = modifier
            .horizontalScroll(scrollState, true),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        for (i in 0 until dotCount) {
            Surface(
                modifier = Modifier
                    .size(12.dp),
                shape = CircleShape,
                color = if (i == currentSelect) Color.Gray else Color.White,
                elevation = 1.dp
            ) {}
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}

@Composable
fun SplashScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val isInstalled = mainViewModel.isInstalled.value
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LightBg)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_splash_screen),
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.77f)
            ,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_adaptive_fore),
                contentDescription = "logo",
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier.size(LogoSplashSize)
            )
            Spacer(modifier = Modifier.size(SmallSpacing))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = DarkBlue,
                            fontFamily = RobotoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = SplashTextSize
                        )
                    ) {
                        append("OCEAN")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = LightBg,
                            fontFamily = RobotoFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = SplashTextSize
                        )
                    ) {
                        append("BIN")
                    }
                }
            )
        }
    }
    LaunchedEffect(key1 = isInstalled) {
        delay(800L)
        if (isInstalled) {
            navController.navigate(Screen.SplashViewPager.route) {
                popUpTo(Screen.SplashScreen.route) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(Screen.SplashScreen.route) {
                    inclusive = true
                }
            }
        }
    }
}
