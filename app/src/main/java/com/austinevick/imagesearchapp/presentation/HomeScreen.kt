package com.austinevick.imagesearchapp.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel = koinViewModel()
) {

    val images = viewModel.images.collectAsLazyPagingItems()
    val query = viewModel.searchQuery.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val image = remember { mutableStateOf("") }
    val title = remember { mutableStateOf("") }



    SharedTransitionLayout {
        AnimatedContent(showDialog.value) { targetState ->
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            TextField(
                                value = query.value,
                                onValueChange = viewModel::setQuery,
                                placeholder = {
                                    Text(
                                        text = "Search Images",
                                        color = Color.Gray,
                                        fontWeight = FontWeight.W600
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.White,
                                    unfocusedIndicatorColor = Color.White,
                                    disabledIndicatorColor = Color.White,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    cursorColor = Color.Black,
                                )
                            )
                        }
                    )
                }
            ) { innerPadding ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {

                    if (images.loadState.refresh is LoadState.Loading) {
                        CircularProgressIndicator()
                    }


                    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {

                        if (images.loadState.prepend is LoadState.Error) {
                            item {
                                Button(onClick = { images.retry() }) {
                                    Text(text = "Retry")
                                }
                            }
                        }

                        if (images.loadState.refresh is LoadState.NotLoading) {
                            items(
                                images.itemCount,
                                key = images.itemKey { it.uuid },
                                contentType = images.itemContentType { "content_type" }
                            ) { index ->
                                images[index]?.let {
                                    if (!targetState)
                                        MainContent(
                                            onShowDetails = {
                                                showDialog.value = true
                                                image.value = it.imageUrl
                                                title.value = it.title
                                            },
                                            image = it.imageUrl,
                                            sharedTransitionScope = this@SharedTransitionLayout,
                                            animatedVisibilityScope = this@AnimatedContent
                                        )
                                }
                            }
                        }
                    }
                }
            }
            if (targetState) {
                Details(
                    onBack = { showDialog.value = false },
                    image = image.value,
                    title = title.value,
                    onDownload = { viewModel.downloadImage(image.value) },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedContent
                )

            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainContent(
    onShowDetails: () -> Unit,
    modifier: Modifier = Modifier,
    image: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = modifier
                .clickable(onClick = onShowDetails)
                .sharedElement(
                    rememberSharedContentState(key = image),
                    animatedVisibilityScope = animatedVisibilityScope
                ),
            contentScale = ContentScale.Crop
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Details(
    onBack: () -> Unit,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier,
    image: String,
    title: String? = null,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Box {
        with(sharedTransitionScope) {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = modifier
                    .clickable(onClick = onBack)
                    .sharedElement(
                        rememberSharedContentState(key = image),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                contentScale = ContentScale.Crop
            )
            Text(title?:"", color = Color.White, modifier = Modifier
                .align(Alignment.BottomCenter).padding(10.dp))
            Button(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp),
                onClick = onDownload) {
                Text(text = "Download image")
            }

        }
    }
}
