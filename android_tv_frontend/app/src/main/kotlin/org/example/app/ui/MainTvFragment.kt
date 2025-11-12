package org.example.app.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.example.app.R
import org.example.app.data.MatchRepository
import org.example.app.model.Match

/**
 * Main TV fragment showing rows of ongoing matches grouped by league.
 * Uses Firebase (or mock fallback) for real-time updates.
 */
class MainTvFragment : BrowseSupportFragment() {

    private lateinit var repo: MatchRepository
    private val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    private lateinit var imageLoader: ImageLoader

    companion object {
        private const val ARG_FIREBASE = "arg_firebase"

        fun newInstance(firebaseInitialized: Boolean): MainTvFragment {
            val f = MainTvFragment()
            f.arguments = Bundle().apply { putBoolean(ARG_FIREBASE, firebaseInitialized) }
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.app_name)
        brandColor = ContextCompat.getColor(requireContext(), R.color.lb_default_brand_color)
        headersState = HEADERS_DISABLED
        adapter = rowsAdapter

        imageLoader = ImageLoader.Builder(requireContext())
            .components { add(SvgDecoder.Factory()) }
            .build()

        repo = MatchRepository(arguments?.getBoolean(ARG_FIREBASE) == true)
        setOnItemViewClickedListener(ItemClickedListener())

        observeMatches()
    }

    private fun observeMatches() {
        viewLifecycleOwner.lifecycleScope.launch {
            repo.watchMatches().collectLatest { matches ->
                buildRows(matches)
            }
        }
    }

    private fun buildRows(matches: List<Match>) {
        rowsAdapter.clear()
        val byLeague = matches.groupBy { it.league }
        byLeague.forEach { (league, list) ->
            val cardPresenter = MatchCardPresenter(imageLoader)
            val rowAdapter = ArrayObjectAdapter(cardPresenter)
            list.forEach { rowAdapter.add(it) }
            val headerItem = HeaderItem(league)
            rowsAdapter.add(ListRow(headerItem, rowAdapter))
        }
    }

    private inner class ItemClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            if (item is Match) {
                val intent = Intent(requireContext(), MatchDetailsActivity::class.java)
                intent.putExtra(MatchDetailsActivity.EXTRA_MATCH_ID, item.id)
                intent.putExtra(MatchDetailsActivity.EXTRA_MATCH_LEAGUE, item.league)
                intent.putExtra(MatchDetailsActivity.EXTRA_MATCH_HOME_NAME, item.home.name)
                intent.putExtra(MatchDetailsActivity.EXTRA_MATCH_HOME_LOGO, item.home.logoUrl)
                intent.putExtra(MatchDetailsActivity.EXTRA_MATCH_AWAY_NAME, item.away.name)
                intent.putExtra(MatchDetailsActivity.EXTRA_MATCH_AWAY_LOGO, item.away.logoUrl)
                intent.putExtra(MatchDetailsActivity.EXTRA_MATCH_SCORE, item.score)
                intent.putExtra(MatchDetailsActivity.EXTRA_MATCH_START_TIME, item.startTime)
                intent.putExtra(MatchDetailsActivity.EXTRA_MATCH_HIGHLIGHT, item.highlightUrl)
                startActivity(intent)
            }
        }
    }
}

/**
 * Card presenter optimized for TV focus and D-pad navigation.
 */
class MatchCardPresenter(private val imageLoader: ImageLoader) : Presenter() {
    override fun onCreateViewHolder(parent: android.view.ViewGroup): ViewHolder {
        val ctx = parent.context
        val cardView = ImageCardView(ctx).apply {
            isFocusable = true
            isFocusableInTouchMode = true
            setMainImageDimensions(320, 180)
        }
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val cardView = viewHolder.view as ImageCardView
        val match = item as org.example.app.model.Match

        cardView.titleText = "${match.home.name} vs ${match.away.name}"
        cardView.contentText = "Score: ${match.score}"

        // Load a composite image: prefer home logo; if not available, fallback
        val logo = match.home.logoUrl.takeIf { it.isNotBlank() } ?: match.away.logoUrl
        val request = ImageRequest.Builder(cardView.context)
            .data(logo.ifBlank { "https://placehold.co/320x180?text=${match.league}" })
            .target(
                onSuccess = { d: Drawable ->
                    cardView.mainImage = d
                },
                onError = {
                    cardView.mainImage =
                        ContextCompat.getDrawable(cardView.context, R.drawable.ic_loop)
                }
            )
            .build()
        imageLoader.enqueue(request)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        cardView.mainImage = null
    }
}
