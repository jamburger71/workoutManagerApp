package uk.ac.aber.dcs.cs31620.assignment.ui.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

const val TAG = "ExerciseDataBaseManager"

const val DB_VERSION = 1
const val DB_NAME = "exerciseProgramDataBase"

const val EXERCISE_TABLE_NAME = "exercises"
const val EXERCISE_ID = "exerciseID"
const val EXERCISE_NAME = "name"
const val REPS = "reps"
const val SETS = "sets"
const val WEIGHT = "weight"
const val ICON = "icon"
const val ICON_COLOR = "iconColor"

const val WORKOUT_TABLE_NAME = "workouts"
const val WORKOUT_DAY = "workoutDay"
const val WORKOUT_NAME = "workoutName"

const val PAIR_TABLE_NAME = "pairs"
const val PAIR_ID = "pairID"
const val POSITION_IN_WORKOUT = "posInWorkout"

class ExerciseDataBaseManager(context: Context, database: String = DB_NAME): SQLiteOpenHelper(context, database, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        createDatabases()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $EXERCISE_TABLE_NAME;")
        db.execSQL("DROP TABLE IF EXISTS $WORKOUT_TABLE_NAME;")
        db.execSQL("DROP TABLE IF EXISTS $PAIR_TABLE_NAME;")

        onCreate(db)
        db.close()
    }

    private fun createExerciseDatabase() {
        val db = this.readableDatabase
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $EXERCISE_TABLE_NAME"
                    + " ("
                    + "$EXERCISE_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "$EXERCISE_NAME TEXT,"
                    + "$REPS INTEGER,"
                    + "$SETS INTEGER,"
                    + "$WEIGHT INTEGER,"
                    + "$ICON BIGINT,"
                    + "$ICON_COLOR INTEGER"
                    + ");"
        )
        Log.i(TAG, "Exercise Database created")
        db.close()
    }

    private fun createWorkoutDatabase() {
        val db = this.readableDatabase
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $WORKOUT_TABLE_NAME"
                    + " ("
                    + "$WORKOUT_DAY TEXT PRIMARY KEY,"
                    + "$WORKOUT_NAME TEXT"
                    + ");"
        )
        Log.i(TAG, "Workout Database created")
        db.execSQL(
            "INSERT INTO $WORKOUT_TABLE_NAME VALUES " +
                    "('Monday', '')," +
                    "('Tuesday', '')," +
                    "('Wednesday', '')," +
                    "('Thursday', '')," +
                    "('Friday', '')," +
                    "('Saturday', '')," +
                    "('Sunday', '');"
        )
        Log.i(TAG, "Workout Database populated")
        db.close()
    }

    private fun createPairingDatabase() {
        val db = this.readableDatabase

        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $PAIR_TABLE_NAME"
                    + " ("
                    + "$PAIR_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "$WORKOUT_DAY TEXT,"
                    + "$EXERCISE_ID INT,"
                    + "$POSITION_IN_WORKOUT INT"
                    + ");"
        )
        Log.i(TAG, "Pairing Database created")
        db.close()
    }
    private fun createDatabases() {
        createExerciseDatabase()
        createWorkoutDatabase()
        createPairingDatabase()
    }

    fun modifyExercise(exercise: Exercise) {
        val db = this.readableDatabase
        db.execSQL(
            "UPDATE $EXERCISE_TABLE_NAME SET " +
                "$EXERCISE_NAME = '${exercise.name}', " +
                "$SETS = '${exercise.sets}', " +
                "$REPS = '${exercise.reps}', " +
                "$WEIGHT = '${exercise.weightInKilos}', " +
                "$ICON = '${exercise.icon}'," +
                "$ICON_COLOR = '${exercise.iconColor}'" +
            "WHERE $EXERCISE_ID = '${exercise.id}';"
        )
        db.close()
    }

    fun addExercise(
        name: String,
        reps: Int,
        sets: Int,
        weight: Int,
        icon: Int,
        iconColor: Int
    ) {
        val exerciseValues = ContentValues()

        exerciseValues.put(EXERCISE_NAME, (if (name != "")name else "Unnamed Exercise"))
        exerciseValues.put(REPS, reps)
        exerciseValues.put(SETS, sets)
        exerciseValues.put(WEIGHT, weight)
        exerciseValues.put(ICON, icon)
        exerciseValues.put(ICON_COLOR, iconColor)

        val db = this.readableDatabase
        db.execSQL("INSERT INTO $EXERCISE_TABLE_NAME ($EXERCISE_NAME,$REPS,$SETS,$WEIGHT,$ICON,$ICON_COLOR) " +
                "VALUES('$name',$reps,$sets,$weight,$icon,$iconColor);")
        Log.i(TAG, "Added new exercise $name to database")
        db.close()
    }

    fun deleteExercise(exercise: Exercise) {
        val db = this.readableDatabase
        db.execSQL("DELETE FROM $EXERCISE_TABLE_NAME WHERE $EXERCISE_ID = ${exercise.id};")
        db.close()
    }

    fun getExercise(id: Int): Exercise {
        val db = this.readableDatabase
        val exerciseCursor: Cursor = db.rawQuery("SELECT * FROM $EXERCISE_TABLE_NAME WHERE $EXERCISE_ID = $id;", null)
        exerciseCursor.moveToFirst()
        val exercise = Exercise(
            id = exerciseCursor.getInt(0),
            name = exerciseCursor.getString(1),
            reps = exerciseCursor.getInt(2),
            sets = exerciseCursor.getInt(3),
            weightInKilos = exerciseCursor.getInt(4),
            icon = exerciseCursor.getInt(5),
            iconColor = exerciseCursor.getInt(6)
        )
        exerciseCursor.close()
        db.close()
        return exercise
    }

    fun getAllExercises(): ArrayList<Exercise> {
        val exercisesArray = ArrayList<Exercise>()
        val db = this.readableDatabase
        val exerciseCursor: Cursor = db.rawQuery("SELECT * FROM $EXERCISE_TABLE_NAME;", null)
        if (exerciseCursor.moveToFirst()) {
            do {
                exercisesArray.add(
                    Exercise(
                        id = exerciseCursor.getInt(0),
                        name = exerciseCursor.getString(1),
                        reps = exerciseCursor.getInt(2),
                        sets = exerciseCursor.getInt(3),
                        weightInKilos = exerciseCursor.getInt(4),
                        icon = exerciseCursor.getInt(5),
                        iconColor = exerciseCursor.getInt(6)
                    )
                )
            } while (exerciseCursor.moveToNext())
        }
        exerciseCursor.close()
        db.close()
        return exercisesArray
    }

    fun createNewPairing(exercise: Exercise, workoutDay: WorkoutDay, workoutPosition: Int) {
        val db = this.readableDatabase
        db.execSQL("INSERT INTO $PAIR_TABLE_NAME($EXERCISE_ID, $WORKOUT_DAY, $POSITION_IN_WORKOUT) VALUES('${exercise.id}','${workoutDay.day}',${workoutDay.listOfExercise.size+1});")
        Log.i(TAG, "New Pairing made between workout ${workoutDay.day} and exercise ${exercise.name}:${exercise.id} with workout position of " + workoutPosition)
        db.close()
    }

    fun getPairingsForWorkout(workoutDay: WorkoutDay): ArrayList<ExerciseWorkoutPairing> {
        val pairingsArray = ArrayList<ExerciseWorkoutPairing>()
        val db = this.readableDatabase
        val exerciseCursor: Cursor = db.rawQuery(
            "SELECT * " +
                    "FROM $PAIR_TABLE_NAME pairTable " +
                    "WHERE $WORKOUT_DAY = '${workoutDay.day}'",null)
        if (exerciseCursor.moveToFirst()) {
            do {
                Log.i(TAG, "workout ${workoutDay.day} exercise ${exerciseCursor.getString(1)}")
                pairingsArray.add(
                    ExerciseWorkoutPairing(
                        pairID = exerciseCursor.getInt(0),
                        workoutDay = exerciseCursor.getString(1),
                        exerciseID = exerciseCursor.getInt(2),
                        positionInWorkout = exerciseCursor.getInt(3)
                    )
                )
            } while (exerciseCursor.moveToNext())
        }
        exerciseCursor.close()
        db.close()
        return pairingsArray
    }

    fun changePairingPosition(pairing: ExerciseWorkoutPairing) {
        val db = this.readableDatabase
        db.execSQL("UPDATE $PAIR_TABLE_NAME " +
                "SET $POSITION_IN_WORKOUT = ${pairing.positionInWorkout} " +
                "WHERE $PAIR_ID = ${pairing.pairID}"
        )
        db.close()
    }

    fun deletePairing(pairing: ExerciseWorkoutPairing) {
        val db = this.readableDatabase
        db.execSQL("DELETE FROM $PAIR_TABLE_NAME WHERE $PAIR_ID = ${pairing.pairID} ")
        db.close()
    }

    fun deletePairingsForExercise(exercise: Exercise) {
        val db = this.readableDatabase
        db.execSQL("DELETE FROM $PAIR_TABLE_NAME WHERE $EXERCISE_ID = ${exercise.id} ")
        db.close()
    }

    fun clearExercisesForWorkout(workoutDay: WorkoutDay) {
        val db = this.readableDatabase
        try {
            db.execSQL("DELETE FROM $PAIR_TABLE_NAME WHERE " +
                    "$WORKOUT_DAY = '${workoutDay.day}';")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        db.close()

    }
    fun getWorkout(day: String): WorkoutDay {
        val db = this.readableDatabase
        val exerciseCursor: Cursor = db.rawQuery("SELECT * FROM $WORKOUT_TABLE_NAME WHERE $WORKOUT_DAY = '$day';", null)
        exerciseCursor.moveToFirst()
        val workout = WorkoutDay(
            day = exerciseCursor.getString(0),
            workoutName =  exerciseCursor.getString(1)
        )
        exerciseCursor.close()
        db.close()
        return workout
    }

    fun getAllWorkouts(): ArrayList<WorkoutDay> {
        val db = this.readableDatabase
        val workoutArray = ArrayList<WorkoutDay>()
        val exerciseCursor: Cursor = db.rawQuery("SELECT * FROM $WORKOUT_TABLE_NAME", null)
        if (exerciseCursor.moveToFirst()) {
            do {
                workoutArray.add(
                    WorkoutDay(
                        day = exerciseCursor.getString(0),
                        workoutName = exerciseCursor.getString(1)
                    )
                )
            } while (exerciseCursor.moveToNext())
        }
        exerciseCursor.close()
        db.close()
        return workoutArray
    }

    fun changeWorkoutName(name: String, workoutDay: WorkoutDay) {
        val db = this.readableDatabase
        db.execSQL("UPDATE $WORKOUT_TABLE_NAME " +
                "SET $WORKOUT_NAME = '$name' " +
                "WHERE $WORKOUT_DAY = '${workoutDay.day}';")

        Log.i(TAG, "Workout \"" + workoutDay.day + "\" name changed to \"" + workoutDay.workoutName + "\"")
    }

    fun clearAllExercises() {
        val db = this.readableDatabase
        db.execSQL("DELETE FROM $EXERCISE_TABLE_NAME")

        Log.w(TAG, "Exercises Database cleared")
        clearAllPairings()
    }
    fun clearAllWorkouts() {
        val db = this.readableDatabase
        db.execSQL("DELETE FROM $WORKOUT_TABLE_NAME")
        Log.w(TAG, "Workouts Database cleared")

        clearAllPairings()
        createWorkoutDatabase()
    }

    private fun clearAllPairings() {
        val db = this.readableDatabase
        db.execSQL("DROP TABLE IF EXISTS $PAIR_TABLE_NAME")
        Log.w(TAG, "Pairings Database cleared")
        createPairingDatabase()
    }

    fun clearAllData() {
        clearAllExercises()
        clearAllWorkouts()
    }
}