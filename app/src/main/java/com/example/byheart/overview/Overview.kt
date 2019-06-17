package com.example.byheart.overview

import com.example.byheart.pile.Pile
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class Overview(title: String, items: List<Pile>) : ExpandableGroup<Pile>(title, items)