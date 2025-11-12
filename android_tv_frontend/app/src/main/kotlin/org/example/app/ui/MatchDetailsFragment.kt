package org.example.app.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.widget.*
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import org.example.app.R

/**
 * Shows details for a selected match along with a "Play Highlights" action.
 */
class MatchDetailsFragment : DetailsSupportFragment() {

    private lateinit var rowsAdapter: ArrayObjectAdapter
    private lateinit var presenterSelector: ClassPresenterSelector
    private lateinit var imageLoader: ImageLoader

    private var highlightUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageLoader = ImageLoader.Builder(requireContext())
            .components { add(SvgDecoder.Factory()) }
            .build()

        val home = requireArguments().getString(MatchDetailsActivity.EXTRA_MATCH_HOME_NAME, "")
        val away = requireArguments().getString(MatchDetailsActivity.EXTRA_MATCH_AWAY_NAME, "")
        val homeLogo = requireArguments().getString(MatchDetailsActivity.EXTRA_MATCH_HOME_LOGO, "")
        val awayLogo = requireArguments().getString(MatchDetailsActivity.EXTRA_MATCH_AWAY_LOGO, "")
        val score = requireArguments().getString(MatchDetailsActivity.EXTRA_MATCH_SCORE, "0-0")
        val league = requireArguments().getString(MatchDetailsActivity.EXTRA_MATCH_LEAGUE, "General")
        val startTime = requireArguments().getString(MatchDetailsActivity.EXTRA_MATCH_START_TIME, "")
        highlightUrl = requireArguments().getString(MatchDetailsActivity.EXTRA_MATCH_HIGHLIGHT, null)

        val detailsOverview = DetailsOverviewRow("$home vs $away")
        detailsOverview.subtitle = "$league  •  $startTime"
        detailsOverview.imageDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.lb_ic_play)

        val description = "Score: $score\n" +
                "League: $league\n" +
                "Start: $startTime\n"

        val actionAdapter = ArrayObjectAdapter()
        if (!highlightUrl.isNullOrBlank()) {
            actionAdapter.add(Action(1, "Play Highlights"))
        }
        detailsOverview.actionsAdapter = actionAdapter

        val overviewPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())
        overviewPresenter.backgroundColor = ContextCompat.getColor(requireContext(), R.color.lb_default_brand_color)
        overviewPresenter.setOnActionClickedListener { action ->
            if (action.id == 1L && !highlightUrl.isNullOrBlank()) {
                val intent = Intent(requireContext(), HighlightPlayerActivity::class.java)
                intent.putExtra(HighlightPlayerActivity.EXTRA_URL, highlightUrl)
                startActivity(intent)
            }
        }

        presenterSelector = ClassPresenterSelector().apply {
            addClassPresenter(DetailsOverviewRow::class.java, overviewPresenter)
            addClassPresenter(ListRow::class.java, ListRowPresenter())
        }

        rowsAdapter = ArrayObjectAdapter(presenterSelector).apply {
            add(detailsOverview)

            val row = ArrayObjectAdapter(DetailsInfoPresenter())
            row.add(DetailsInfo(description, homeLogo, awayLogo))
            add(ListRow(HeaderItem("Match Info"), row))
        }
        adapter = rowsAdapter
    }

    class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {
        override fun onBindDescription(vh: ViewHolder, item: Any) {
            val title = item as String
            vh.title.text = title
            vh.body.text = "Press Play to watch highlights if available."
        }
    }

    data class DetailsInfo(val text: String, val homeLogo: String, val awayLogo: String)

    class DetailsInfoPresenter : Presenter() {
        override fun onCreateViewHolder(parent: android.view.ViewGroup): ViewHolder {
            val v = android.view.LayoutInflater.from(parent.context)
                .inflate(R.layout.item_details_info, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
            val info = item as DetailsInfo
            val tv = viewHolder.view.findViewById<android.widget.TextView>(R.id.info_text)
            tv.text = info.text

            val homeIv = viewHolder.view.findViewById<android.widget.ImageView>(R.id.home_logo)
            val awayIv = viewHolder.view.findViewById<android.widget.ImageView>(R.id.away_logo)

            val ctx = viewHolder.view.context
            val loader = ImageLoader.Builder(ctx).components { add(SvgDecoder.Factory()) }.build()
            val makeReq = { url: String, target: android.widget.ImageView ->
                val req = coil.request.ImageRequest.Builder(ctx)
                    .data(url.ifBlank { "https://placehold.co/128x128?text=HOME" })
                    .target(
                        onSuccess = { d: Drawable -> target.setImageDrawable(d) },
                        onError = { target.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.lb_ic_loop)) }
                    ).build()
                loader.enqueue(req)
            }
            makeReq(info.homeLogo, homeIv)
            makeReq(info.awayLogo, awayIv)
        }

        override fun onUnbindViewHolder(viewHolder: ViewHolder) {}
    }
}
