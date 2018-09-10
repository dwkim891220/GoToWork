package android.dwkim.pr.gotothesugarhill.util

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by dowoo-kim on 2018. 1. 15..
 */
abstract class AndroidExtensionsViewHolder(override val containerView: View)
    : RecyclerView.ViewHolder(containerView), LayoutContainer