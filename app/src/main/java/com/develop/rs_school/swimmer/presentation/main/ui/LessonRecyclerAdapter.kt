package com.develop.rs_school.swimmer.presentation.main.ui

import android.graphics.Color.parseColor
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.develop.rs_school.swimmer.R
import com.develop.rs_school.swimmer.databinding.RecyclerViewRawBinding
import com.develop.rs_school.swimmer.domain.AgendaStatus
import com.develop.rs_school.swimmer.domain.Lesson
import com.develop.rs_school.swimmer.util.getCapitalizingDayOfWeek
import com.develop.rs_school.swimmer.util.getDateDDMM

class LessonRecyclerAdapter(private val itemClickListener: LessonRecyclerItemListener) :
    ListAdapter<Lesson, LessonRecyclerAdapter.ViewHolder>(
        LessonDiffUtilCallback()
    ) {

    private companion object {
        private const val NEUTRAL_COLOR = "#2E7D32"
        private const val BAD_COLOR = "#C62828"
        private const val PLANED_COLOR = "#575757" // FIXME WTF
        private const val FREE_COLOR = "#FFA000"
    }

    inner class ViewHolder(private val itemBinding: RecyclerViewRawBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                // getItem(adapterPosition)?.let { itemClickListener.onClick(it) }
            }
        }

        fun bind(lesson: Lesson) {
            // start view appearance
            itemBinding.weekDay.visibility = View.VISIBLE
            itemBinding.weekDay.paintFlags =
                itemBinding.weekDay.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            itemBinding.date.paintFlags =
                itemBinding.date.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            itemBinding.image.colorFilter = null
            itemBinding.icon.setImageDrawable(null)

            lesson.date?.let {
                itemBinding.date.text = getDateDDMM(it)
                itemBinding.weekDay.text = getCapitalizingDayOfWeek(it)
            }

            setItemImageByType(itemBinding, lesson.type)

            when (lesson.agendaStatus) {
                AgendaStatus.NONE -> {
                    itemView.setBackgroundResource(R.drawable.layout_border_white)
                    itemBinding.date.text = "Сейчас"
                    itemBinding.weekDay.visibility = View.GONE
                    itemBinding.image.setImageResource(R.drawable.ion_flag)
                    itemBinding.image.setColorFilter(parseColor(NEUTRAL_COLOR))
                }
                AgendaStatus.MISSED_FREE -> {
                    itemView.setBackgroundResource(R.drawable.layout_border_yellow)
                    itemBinding.icon.setImageResource(R.drawable.ic_baseline_close_24)
                    itemBinding.image.setColorFilter(parseColor(FREE_COLOR))
                }
                AgendaStatus.CANCELED -> {
                    itemView.setBackgroundResource(R.drawable.layout_border_gray)
                    itemBinding.icon.setImageResource(
                        R.drawable.ic_baseline_remove_circle_outline_24
                    )
                    itemBinding.date.paintFlags =
                        itemBinding.date.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    itemBinding.weekDay.paintFlags =
                        itemBinding.weekDay.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                AgendaStatus.PAUSE -> {
                    itemView.setBackgroundResource(R.drawable.layout_border_gray)
                    itemBinding.icon.setImageResource(R.drawable.ic_baseline_pause_24)
                    itemBinding.date.paintFlags =
                        itemBinding.date.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    itemBinding.weekDay.paintFlags =
                        itemBinding.weekDay.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                AgendaStatus.PLANNED -> {
                    itemView.setBackgroundResource(R.drawable.layout_border_gray)
                    itemBinding.image.setColorFilter(parseColor(PLANED_COLOR))
                }
                AgendaStatus.FORGOT -> {
                    itemView.setBackgroundResource(R.drawable.layout_border_gray)
                    itemBinding.icon.setImageResource(R.drawable.question)
                }
                AgendaStatus.MISSED_NOT_PAID -> {
                    itemView.setBackgroundResource(R.drawable.layout_border_red)
                    itemBinding.icon.setImageResource(R.drawable.ic_baseline_close_24)
                    itemBinding.image.setColorFilter(parseColor(BAD_COLOR))
                }
                AgendaStatus.MISSED_PAID -> {
                    itemView.setBackgroundResource(R.drawable.layout_border_yellow)
                    itemBinding.icon.setImageResource(R.drawable.ic_baseline_close_24)
                    itemBinding.image.setColorFilter(parseColor(BAD_COLOR))
                }
                AgendaStatus.PREPAID -> {
                    itemView.setBackgroundResource(R.drawable.layout_border_green)
                    itemBinding.image.setColorFilter(parseColor(NEUTRAL_COLOR))
                }
                AgendaStatus.VISIT_PAID -> {
                    itemView.setBackgroundResource(R.drawable.layout_border_green)
                    itemBinding.icon.setImageResource(R.drawable.ic_baseline_done_24)
                    itemBinding.image.setColorFilter(parseColor(NEUTRAL_COLOR))
                }
                AgendaStatus.VISIT_NOT_PAID -> {
                    itemView.setBackgroundResource(R.drawable.layout_border_red)
                    itemBinding.icon.setImageResource(R.drawable.ic_baseline_done_24)
                    itemBinding.image.setColorFilter(parseColor(BAD_COLOR))
                }
            }
        }
    }

    private fun setItemImageByType(itemBinding: RecyclerViewRawBinding, type: String) {
        when (type) {
            "1" -> itemBinding.image.setImageResource(R.drawable.person_grey)
            "2" -> itemBinding.image.setImageResource(R.drawable.person_stalker)
            "3" -> itemBinding.image.setImageResource(R.drawable.asterisk)
            "4" -> itemBinding.image.setImageResource(R.drawable.paperplanenew)
            "5" -> itemBinding.image.setImageResource(R.drawable.md_contacts)
            "6" -> itemBinding.image.setImageResource(R.drawable.people)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewRawBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rssItem = getItem(position)
        rssItem?.let { holder.bind(it) }
    }
}

class LessonDiffUtilCallback : DiffUtil.ItemCallback<Lesson>() {
    override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
        return oldItem.id == newItem.id
    }
}

class LessonRecyclerItemListener(val clickListener: (lesson: Lesson) -> Unit) {
    fun onClick(lesson: Lesson) = clickListener(lesson)
}