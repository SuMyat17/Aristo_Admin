import androidx.lifecycle.ViewModel
import com.smtz.aristo.admin.model.*
import com.smtz.aristo.admin.view.dummyData.*

class CategoriesViewModel : ViewModel() {

    val dummyDataList: ArrayList<Category> = arrayListOf()
    fun loadCategories(){

        dummyDataList.add(dummyCategory1)
        dummyDataList.add(dummyCategory2)
        dummyDataList.add(dummyCategory3)
        dummyDataList.add(dummyCategory4)

    }
}