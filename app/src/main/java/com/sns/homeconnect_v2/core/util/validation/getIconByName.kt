package com.sns.homeconnect_v2.core.util.validation

import androidx.annotation.DrawableRes
import com.sns.homeconnect_v2.R

/* ----------  TEXT  →  DRAWABLE ID  ---------- */

@DrawableRes
fun getIconResByName(name: String?): Int = when (name?.lowercase()) {

    /* GROUP */
    "company"       -> R.drawable.ic_company
    "family"        -> R.drawable.ic_family
    "government"    -> R.drawable.ic_government
    "organization"  -> R.drawable.ic_organization
    "personal"      -> R.drawable.ic_personal
    "school_group"  -> R.drawable.ic_school_group

    /* HOUSE */
    "house"         -> R.drawable.ic_house
    "apartment"     -> R.drawable.ic_apartment
    "villa"         -> R.drawable.ic_villa
    "office"        -> R.drawable.ic_office
    "factory"       -> R.drawable.ic_factory
    "hotel"         -> R.drawable.ic_hotel
    "school"        -> R.drawable.ic_school
    "store"         -> R.drawable.ic_store   // dùng chung cho HOUSE & SPACE

    /* SPACE */
    "kitchen"       -> R.drawable.ic_kitchen
    "bedroom"       -> R.drawable.ic_bedroom
    "livingroom"    -> R.drawable.ic_livingroom
    "bathroom"      -> R.drawable.ic_bathroom
    "balcony"       -> R.drawable.ic_balcony
    "garage"        -> R.drawable.ic_garage
    "garden"        -> R.drawable.ic_garden
    "hallway"       -> R.drawable.ic_hallway
    "lobby"         -> R.drawable.ic_lobby
    "rooftop"       -> R.drawable.ic_rooftop
    "entertainment" -> R.drawable.ic_entertainment
    "workroom"      -> R.drawable.ic_workroom

    /* fallback */
    else            -> R.drawable.ic_house
}

/* ----------  DRAWABLE ID  →  TEXT  ---------- */

fun Int.toIconName(): String = when (this) {

    /* GROUP */
    R.drawable.ic_company        -> "company"
    R.drawable.ic_family         -> "family"
    R.drawable.ic_government     -> "government"
    R.drawable.ic_organization   -> "organization"
    R.drawable.ic_personal       -> "personal"
    R.drawable.ic_school_group   -> "school_group"

    /* HOUSE */
    R.drawable.ic_house          -> "house"
    R.drawable.ic_apartment      -> "apartment"
    R.drawable.ic_villa          -> "villa"
    R.drawable.ic_office         -> "office"
    R.drawable.ic_factory        -> "factory"
    R.drawable.ic_hotel          -> "hotel"
    R.drawable.ic_school         -> "school"
    R.drawable.ic_store          -> "store"

    /* SPACE */
    R.drawable.ic_kitchen        -> "kitchen"
    R.drawable.ic_bedroom        -> "bedroom"
    R.drawable.ic_livingroom     -> "livingroom"
    R.drawable.ic_bathroom       -> "bathroom"
    R.drawable.ic_balcony        -> "balcony"
    R.drawable.ic_garage         -> "garage"
    R.drawable.ic_garden         -> "garden"
    R.drawable.ic_hallway        -> "hallway"
    R.drawable.ic_lobby          -> "lobby"
    R.drawable.ic_rooftop        -> "rooftop"
    R.drawable.ic_entertainment  -> "entertainment"
    R.drawable.ic_workroom       -> "workroom"

    else                         -> "house"
}
