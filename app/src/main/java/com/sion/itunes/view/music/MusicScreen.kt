package com.sion.itunes.view.music

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Movie
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.sion.itunes.R
import com.sion.itunes.model.vo.Music
import com.sion.itunes.state.ErrorItem
import com.sion.itunes.state.LoadingItem
import com.sion.itunes.state.LoadingView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

//Ref : https://foso.github.io/Jetpack-Compose-Playground/layout/constraintlayout/
//Ref : https://medium.com/android-dev-hacks/exploring-constraint-layout-in-jetpack-compose-67b82123c28b
// Ref : https://medium.com/mobile-app-development-publication/recyclerview-and-lazycolumnfor-in-jetpack-compose-a7842cd7f17e
//Ref : https://levelup.gitconnected.com/jetpack-compose-container-layout-183e655518f2
//Ref : https://iter01.com/573642.html
//Ref : https://medium.com/nerd-for-tech/jetpack-compose-ep-9-progress-indicator-app-14b68fd87a1f
@ExperimentalPagingApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MusicScreen(keyword: String, mainViewModel: MusicViewModel) {
//    Text(text = "Hello world.",color = Color.White )
//    FlexColumn {
//        inflexible {
//            // Item height will be equal content height
//            TopAppBar( // App Bar with title
//                title = { Text("Jetpack Compose Sample") }
//            )
//        }
//        expanded(1F) {
//            // occupy whole empty space in the Column
//            Center {
//                // Center content
//                Text("Hello $name!") // Text label
//            }
//        }
//    }
    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = "PopularMovies") }
//            )
//        },
        content = {
//            MusicView(keyword,mainViewModel)
            MusicList(movies = mainViewModel.search(keyword))
        }
    )
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
}

@OptIn(ExperimentalPagingApi::class)
@Composable
fun MusicView(keyword: String, mainViewModel: MusicViewModel) {
    ConstraintLayout(
        constraintSet = ConstraintSet {
            val recyclelist = createRefFor("rv_music")
            val progressIndicator = createRefFor("progress_bar")
            constrain(recyclelist) {
                top.linkTo(parent.top, margin = 8.dp)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            constrain(progressIndicator) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
//        val guideline = createGuidelineFromStart(0.2f)
//        val (box1, box2) = createRefs()
//
//        Box(
//            modifier = Modifier.fillMaxSize()
//                .background(color = Color.Yellow)
//                .constrainAs(box1) {
//                    end.linkTo(guideline)
//                }
//        )
//
//        Box(
//            modifier = Modifier.fillMaxSize()
//                .background(color = Color.Red)
//                .constrainAs(box2) {
//                    start.linkTo(guideline)
//                }
//        )
        MusicList(
            movies = mainViewModel.search(keyword)
        )
        CircularProgressIndicator(
            modifier = Modifier.layoutId("progress_bar"),
            color = Color.Green,
            strokeWidth = 2.dp
        )
    }
}

//Advanced Ref : https://github.com/dokar3/LazyRecycler
// Ref : https://gist.github.com/shakil807g/13a4074d7589405be35e102e3347db86
// Ref : https://proandroiddev.com/android-jetpack-compose-exploring-state-based-ui-e1d970471d0a
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicList(movies: Flow<PagingData<Music>>) {
    val lazyMovieItems = movies.collectAsLazyPagingItems()
    val state = rememberLazyListState()
////        items(lazyMovieItems) { movie ->
////            ItemMusic(item = movie!!)
////        }
//    LazyVerticalGrid(
//        modifier = Modifier.fillMaxSize(),
//        cells = GridCells.Fixed(2),
//        // contentPadding = PaddingValues(top = Constants.Padding8dp)
//    ) {
//        items(lazyMovieItems.itemCount) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(),
//                contentAlignment = Alignment.Center,
//            ) {
//                Text(
//                    text = "列表項：$it"
//                )
//            }
//        }
//    }

    when (val refreshState = lazyMovieItems.loadState.refresh) {
        is LoadState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
        is LoadState.NotLoading -> {
            Column(modifier = Modifier.fillMaxSize()) {
                if(lazyMovieItems.itemCount > 0) {
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 8.dp, bottom = 8.dp),
                        state = state,
                        content = {
                            items(lazyMovieItems.itemCount) { index ->
                                val music = lazyMovieItems.peek(index)?: return@items
                                ItemMusic(
                                    music
                                )
                            }
                        }
                    )
                } else {

                }
            }
        }
        is LoadState.Error -> {
            ErrorItem(
                modifier = Modifier.fillMaxSize(),
                message = refreshState.toString(),
                onClickRetry = {
//                                retry()
                }
            )
        }
    }
    when (val appendState = lazyMovieItems.loadState.append) {
        is LoadState.Loading -> {
//                        item { LoadingItem() }

        }
        is LoadState.NotLoading -> {

        }
        is LoadState.Error -> {
            val e = lazyMovieItems.loadState.append as LoadState.Error
//            item {
//                ErrorItem(
//                    message = e.error.localizedMessage!!,
//                    onClickRetry = {
////                                    retry()
//                    }
//                )
//            }
        }
    }

//    lazyMovieItems.apply {
//        when {
//            loadState.refresh is LoadState.Loading -> {
//                item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
//            }
//            loadState.append is LoadState.Loading -> {
//                item { LoadingItem() }
//            }
//            loadState.refresh is LoadState.Error -> {
//                val e = lazyMovieItems.loadState.refresh as LoadState.Error
//                item {
//                    ErrorItem(
//                        message = e.error.localizedMessage!!,
//                        modifier = Modifier.fillParentMaxSize(),
//                        onClickRetry = { retry() }
//                    )
//                }
//            }
//            loadState.append is LoadState.Error -> {
//                val e = lazyMovieItems.loadState.append as LoadState.Error
//                item {
//                    ErrorItem(
//                        message = e.error.localizedMessage!!,
//                        onClickRetry = { retry() }
//                    )
//                }
//            }
//        }
//    }

//            items(lazyMovieItems.itemCount) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .fillMaxHeight(),
//                    contentAlignment = Alignment.Center,
//                ) {

//                }
//            }


}

//@OptIn(ExperimentalFoundationApi::class)
//private fun LazyGridScope.renderLoading(loadState: CombinedLoadStates) {
//    when {
//        loadState.refresh is LoadState.Loading -> {
//            item {
//                val title = stringResource(id = R.string.fetching_movies)
//                // TODO: Find a way to fill max size
//                LoadingColumn(title)
//            }
//        }
//        loadState.append is LoadState.Loading -> {
//            item {
//                val title = stringResource(R.string.fetching_more_movies)
//                // TODO: Find a way to fill max width
//                LoadingRow(title = title)
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//private fun LazyGridScope.renderError(loadState: CombinedLoadStates) {
//    when {
//        loadState.refresh is LoadState.Error -> {
//            val error = loadState.refresh as LoadState.Error
//            item {
//                // TODO: Find a way to fill max size
//                ErrorColumn(error.error.message.orEmpty())
//            }
//        }
//        loadState.append is LoadState.Error -> {
//            val error = loadState.append as LoadState.Error
//            item {
//                // TODO: Find a way to fill max width
//                ErrorRow(title = error.error.message.orEmpty())
//            }
//        }
//    }
//}

@Composable
//fun ItemMusic(item: Music, modifier: Modifier = Modifier, onMovieClicked: (Int) -> Unit = {}) {
fun ItemMusic(item: Music  ) {
//    Row(
//        modifier = Modifier
//            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
//            .fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        TrackNameMusic(
//            item.artistName,
//            modifier = Modifier.weight(1f)
//        )
////        ImageViewMusic(
////            BuildConfig.LARGE_IMAGE_URL + movie.backdrop_path,
////            modifier = Modifier.padding(start = 16.dp).preferredSize(90.dp)
////        )
//    }
    ConstraintLayout(
        constraintSet = ConstraintSet {
            val rule_cover = createRefFor("iv_cover")
            val rule_track_name = createRefFor("tv_track_name")
            val rule_artist_name = createRefFor("tv_artist_name")
            constrain(rule_cover) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            constrain(rule_track_name) {
                top.linkTo(rule_cover.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            constrain(rule_artist_name) {
                top.linkTo(rule_track_name.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        },
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
//                modifier= modifier.fillMaxWidth().wrapContentHeight()
    ) {
        ImageViewMusic(
            item.artworkUrl100,
            modifier = Modifier.layoutId("iv_cover"),
        )
        TrackNameMusic(
            item.trackName,
            modifier = Modifier.layoutId("tv_track_name"),
        )
        ArtistNameMusic(
            item.artistName,
            modifier = Modifier.layoutId("tv_artist_name"),
        )
    }
}

@Composable
fun ImageViewMusic(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val tint = if (MaterialTheme.colors.isLight) Color.DarkGray else Color.Gray
    val painter = rememberCoilPainter(request = imageUrl, previewPlaceholder = R.drawable.ic_image)
//    CoilImage(
//        data = imageUrl,
//        modifier = modifier,
//        fadeIn = true,
//        contentScale = ContentScale.Crop,
//        loading = {
//            Image(ImageVector.vectorResource(id = R.drawable.ic_photo), alpha = 0.45f)
//        },
//        error = {
//            Image(ImageVector.vectorResource(id = R.drawable.ic_broken_image), alpha = 0.45f)
//        }
//    )
    Image(
        painter = painter,
        contentDescription = "",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )
    val modifier = Modifier
        .padding(8.dp)
        .fillMaxSize()
    when (painter.loadState) {
        is ImageLoadState.Loading -> {
            Image(
                painter = rememberVectorPainter(image = Icons.Default.Movie),
                contentDescription = null,
                colorFilter = ColorFilter.tint(tint),
                modifier = modifier
            )
        }
        is ImageLoadState.Error -> {
            Image(
                imageVector = Icons.Filled.BrokenImage,
                contentDescription = null,
                colorFilter = ColorFilter.tint(tint),
                modifier = modifier
            )
        }
        else -> {
        }
    }
}

@Composable
fun TrackNameMusic(
    trackname: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = trackname,
        maxLines = 1,
        style = MaterialTheme.typography.subtitle2.copy(
            color = Color.White,
            letterSpacing = 10.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.W400
        ),
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ArtistNameMusic(
    artist: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = artist,
        maxLines = 1,
        letterSpacing = 8.sp,
        style = MaterialTheme.typography.h6,
        overflow = TextOverflow.Ellipsis
    )
}