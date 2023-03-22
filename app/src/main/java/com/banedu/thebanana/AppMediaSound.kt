package com.banedu.thebanana

class AppMediaSound {
    val btnClickSFX = R.raw.clickbuttonsfx
    val studyMusic = arrayListOf(
        musicClass("Relax.mp3", R.raw.relax_music, R.drawable.flowericon),
        musicClass("Banana.mp3", R.raw.bananasong, R.drawable.banana),
        musicClass("MinionOpera.mp3", R.raw.minionopera, R.drawable.minion)
    )
}

data class musicClass(var title: String, var music: Int, var image: Int)
