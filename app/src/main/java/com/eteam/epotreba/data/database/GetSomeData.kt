package com.eteam.epotreba.data.database
import com.eteam.epotreba.domain.models.MarkerModel
import com.google.android.gms.maps.model.LatLng
import net.datafaker.Faker

class GetSomeData {

    fun execute(): List<MarkerModel> {
        val num = 10
        return generateUsersList(num)
    }

    private fun generateUsersList(numUsers: Int): List<MarkerModel> {
        val faker = Faker()
        val marks = mutableListOf<MarkerModel>()

        repeat(numUsers) {
            val title = faker.name().title()
            val position = LatLng(2.1,2.1)
            val about = faker.company().name()
            val user = MarkerModel(title, about, position,0.0)
            marks.add(user)
        }

        return marks
    }


}