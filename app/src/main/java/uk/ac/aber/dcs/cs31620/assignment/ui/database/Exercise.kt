package uk.ac.aber.dcs.cs31620.assignment.ui.database

data class Exercise(
    var id: Int,
    var name: String,
    var reps: Int,
    var sets: Int,
    var weightInKilos: Int,
    var icon: Int,
    var iconColor: Int
)