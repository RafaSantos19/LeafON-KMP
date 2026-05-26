package kmp.edu.leafon_kmp.core.format

import kotlin.math.pow
import kotlin.math.round

fun Double.format(decimals: Int): String {
    val safeDecimals = decimals.coerceAtLeast(0)
    val factor = 10.0.pow(safeDecimals)
    val rounded = round(this * factor) / factor
    return rounded.toString()
}

fun Float.format(decimals: Int): String = toDouble().format(decimals)
