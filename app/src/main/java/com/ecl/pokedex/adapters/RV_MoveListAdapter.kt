package com.ecl.pokedex.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.ecl.pokedex.data.PokemonMoveData
import com.ecl.pokedex.databinding.MoveListItemBinding

class RV_MoveListAdapter: RecyclerView.Adapter<RV_MoveListAdapter.ViewHolder>() {

    private val sortedList = SortedList(PokemonMoveData::class.java, ListCallback())
    private var compareBy: CompareBy = CompareBy.LearnLevel

    enum class CompareBy: ((PokemonMoveData, PokemonMoveData) -> Int) {
        LearnLevel {
            override fun invoke(p1: PokemonMoveData, p2: PokemonMoveData): Int {
                return p1.levelLearnedAt.compareTo(p2.levelLearnedAt)
            }
        },
        LearnLevelDescending {
            override fun invoke(p1: PokemonMoveData, p2: PokemonMoveData): Int {
                return p1.levelLearnedAt.compareTo(p2.levelLearnedAt) * -1
            }
        },
        Name {
            override fun invoke(p1: PokemonMoveData, p2: PokemonMoveData): Int {
                return p1.name.compareTo(p2.name)
            }
        },
        NameDescending {
            override fun invoke(p1: PokemonMoveData, p2: PokemonMoveData): Int {
                return p1.name.compareTo(p2.name) * -1
            }
        },
        Power {
            override fun invoke(p1: PokemonMoveData, p2: PokemonMoveData): Int {
                val p1Pow = p1.power ?: -1
                val p2Pow = p2.power ?: -1
                return p1Pow.compareTo(p2Pow)
            }
        },
        PowerDescending {
            override fun invoke(p1: PokemonMoveData, p2: PokemonMoveData): Int {
                val p1Pow = p1.power ?: -1
                val p2Pow = p2.power ?: -1
                return p1Pow.compareTo(p2Pow) * -1
            }
        },
        Acc {
            override fun invoke(p1: PokemonMoveData, p2: PokemonMoveData): Int {
                val p1Acc = p1.acc ?: 1000
                val p2Acc = p2.acc ?: 1000
                return p1Acc.compareTo(p2Acc)
            }
        },
        AccDescending {
            override fun invoke(p1: PokemonMoveData, p2: PokemonMoveData): Int {
                val p1Acc = p1.acc ?: 1000
                val p2Acc = p2.acc ?: 1000
                return p1Acc.compareTo(p2Acc) * -1
            }
        },
        PP {
            override fun invoke(p1: PokemonMoveData, p2: PokemonMoveData): Int {
                val p1PP = p1.pp ?: 1000
                val p2PP = p2.pp ?: 1000
                return p1PP.compareTo(p2PP)
            }
        },
        PPDescending {
            override fun invoke(p1: PokemonMoveData, p2: PokemonMoveData): Int {
                val p1PP = p1.pp ?: 1000
                val p2PP = p2.pp ?: 1000
                return p1PP.compareTo(p2PP) * -1
            }
        };

        fun reverse(): CompareBy {
            return when (this) {
                LearnLevel -> LearnLevelDescending
                LearnLevelDescending -> LearnLevel
                Name -> NameDescending
                NameDescending -> Name
                Power -> PowerDescending
                PowerDescending -> Power
                Acc -> AccDescending
                AccDescending -> Acc
                PP -> PPDescending
                PPDescending -> PP
            }
        }
    }

    inner class ListCallback: SortedListAdapterCallback<PokemonMoveData>(this) {
        override fun compare(o1: PokemonMoveData, o2: PokemonMoveData): Int {
            return compareBy.invoke(o1, o2)
        }

        override fun areContentsTheSame(
            oldItem: PokemonMoveData,
            newItem: PokemonMoveData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(item1: PokemonMoveData, item2: PokemonMoveData): Boolean {
            return item1.id == item2.id
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            super.onMoved(fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
        }
        override fun onChanged(position: Int, count: Int) {
            super.onChanged(position, count)
            notifyItemRangeChanged(position, count)
        }
        override fun onRemoved(position: Int, count: Int) {
            super.onRemoved(position, count)
            notifyItemRangeRemoved(position, count)
        }
        @SuppressLint("NotifyDataSetChanged")
        override fun onInserted(position: Int, count: Int) {
            super.onInserted(position, count)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(private val binding: MoveListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PokemonMoveData) {
            binding.apply {
                tvName.text = item.name
                tvAcc.text = item.acc?.toString() ?: "--"
                tvPower.text = item.power?.toString() ?: "--"
                tvPp.text = item.pp?.toString() ?: "--"
                tvLevel.text = item.levelLearnedAt.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MoveListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return sortedList.size()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sortedList[position])
    }

    private fun updateComparisonMethod() {
        // Notify the adapter to re-sort the list
        val tmpList: MutableList<PokemonMoveData> = mutableListOf()
        sortedList.beginBatchedUpdates()
        val count = sortedList.size()
        for (i in 0 until count) {
            //sortedList.recalculatePositionOfItemAt(i)
            tmpList.add(sortedList.removeItemAt(0))
        }
        sortedList.addAll(tmpList)
        sortedList.endBatchedUpdates()
    }

    //fun getCompare(): CompareBy = compareBy

    /**
     * Updates the dataset with a new sort method
     *
     * @param compare The order to sort by.
     * @param reverse Should it reverse compare if this compare is set.
     */
    fun sort(compare: CompareBy, reverse: Boolean) {
        //Check they aren't the same
        if (compare.ordinal != compareBy.ordinal) {
            compareBy = compare
            updateComparisonMethod()
        }
        //Optionally reverse if the same
        else if (reverse) {
            compareBy = compareBy.reverse()
            updateComparisonMethod()
        }
    }

    fun replaceDataSet(dataset: List<PokemonMoveData>) {
        sortedList.replaceAll(dataset)
    }

    fun insertData(data: PokemonMoveData) {
        sortedList.add(data)
    }
}