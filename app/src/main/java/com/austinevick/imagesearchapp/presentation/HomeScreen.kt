package com.austinevick.imagesearchapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel = koinViewModel()
) {

    val images = viewModel.images.collectAsLazyPagingItems()
    val query = viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {},
            title = {
                TextField(
                    value = query.value,
                    onValueChange = viewModel::setQuery,
                    placeholder = {
                        Text(text = "Search Images",
                            color = Color.Gray,
                            fontWeight = FontWeight.W600)
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

            if (images.loadState.refresh is LoadState.NotLoading) {
                if (images.itemSnapshotList.isEmpty()) {
                    Text(text = "No Images Found")
                }
            }
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
                            AsyncImage(
                                model = it.imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }

}