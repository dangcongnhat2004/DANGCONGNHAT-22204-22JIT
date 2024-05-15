import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a22it204_dangcongnhat_22jit.R
import com.example.a22it204_dangcongnhat_22jit.RecyclerItemClickListener
import com.example.a22it204_dangcongnhat_22jit.Student
import com.example.a22it204_dangcongnhat_22jit.StudentService
import com.example.a22it204_dangcongnhat_22jit.UserAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageFragment : Fragment() {
    lateinit var rcvUser: RecyclerView
    lateinit var userAdapter: UserAdapter
    lateinit var etSearch: EditText
    private var originalList: List<Student> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find RecyclerView by id
        rcvUser = view.findViewById(R.id.rcv_user)

        // Initialize userAdapter
        userAdapter = UserAdapter(ArrayList())

        // Set layout manager for RecyclerView
        rcvUser.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Set adapter for RecyclerView
        rcvUser.adapter = userAdapter

        etSearch = view.findViewById(R.id.et_search) // Add this line
        // Call API
        callApi()

        // Add OnItemTouchListener for RecyclerView
        deleteStudent()

        view.findViewById<View>(R.id.fab_add).setOnClickListener {
            showAddStudentDialog()
        }
        etSearch.addTextChangedListener { searchText ->
            val query = searchText.toString().trim()
            if (query.isEmpty()) {
                // Nếu `searchText` rỗng, hiển thị lại dữ liệu ban đầu
                userAdapter.updateList(originalList)
                callApi()
            } else {
                // Nếu `searchText` không rỗng, thực hiện tìm kiếm
                filterList(query)
            }
        }


    }
    private fun filterList(searchText: String) {
        if (searchText.isNullOrEmpty()) {
            // Nếu searchText rỗng hoặc null, không thực hiện tìm kiếm và trả về
            return
        }

        val filteredList = userAdapter.listUser.filter { student ->
            // Kiểm tra null trước khi gọi contains
            val nameContains = student.name?.contains(searchText, ignoreCase = true) ?: false
            val addressContains = student.address?.contains(searchText, ignoreCase = true) ?: false
            val dobContains = student.dayOfBirth?.contains(searchText, ignoreCase = true) ?: false
            nameContains || addressContains || dobContains
        }
        userAdapter.filterList(filteredList)
    }






    private fun showAddStudentDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_student, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.et_name)
        val addressEditText = dialogView.findViewById<EditText>(R.id.et_address)
        val dobEditText = dialogView.findViewById<EditText>(R.id.et_dayOfBirth)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Thêm sinh viên mới")
            .setView(dialogView)
            .setPositiveButton("Lưu") { dialog, _ ->
                val name = nameEditText.text.toString()
                val address = addressEditText.text.toString()
                val dob = dobEditText.text.toString()

                // Gọi API để thêm sinh viên mới
                addStudent(name, address, dob)

                dialog.dismiss()
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

        // Đảm bảo hộp thoại không đóng khi nhấn vào nút "Lưu" mà các trường nhập liệu còn trống
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
        nameEditText.addTextChangedListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                !nameEditText.text.isNullOrEmpty() && !addressEditText.text.isNullOrEmpty() && !dobEditText.text.isNullOrEmpty()
        }
        addressEditText.addTextChangedListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                !nameEditText.text.isNullOrEmpty() && !addressEditText.text.isNullOrEmpty() && !dobEditText.text.isNullOrEmpty()
        }
        dobEditText.addTextChangedListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                !nameEditText.text.isNullOrEmpty() && !addressEditText.text.isNullOrEmpty() && !dobEditText.text.isNullOrEmpty()
        }
    }

    private fun addStudent(name: String, address: String, dob: String) {
        val newStudent = Student(id = -1, name = name, image = "2", address = address, dayOfBirth = dob)

        // Gọi API để thêm sinh viên mới
        StudentService.apiUser.addStudent(newStudent).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Nếu thêm thành công, cập nhật RecyclerView để hiển thị sinh viên mới
                    callApi()
                } else {
                    Toast.makeText(requireContext(), "Thêm sinh viên thất bại", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Gặp lỗi khi kết nối đến máy chủ", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun callApi() {
        StudentService.apiUser.getDataUser().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                val list = response.body()
                if (list != null) {
                    userAdapter.listUser = list // Update new data for userAdapter
                    userAdapter.notifyDataSetChanged() // Notify adapter about the change
                }
                Toast.makeText(requireContext(), "Call success", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Toast.makeText(requireContext(), "Call Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    public fun deleteStudent() {
        rcvUser.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                rcvUser,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: RecyclerView.ViewHolder, position: Int) {
                        val selectedStudent = userAdapter.listUser[position]
                        showEditStudentDialog(selectedStudent)
                    }

                    override fun onLongItemClick(view: RecyclerView.ViewHolder, position: Int) {
                        val studentToDelete = userAdapter.listUser[position]

                        val alertDialogBuilder = AlertDialog.Builder(requireContext())
                        alertDialogBuilder.setTitle("Xác nhận xóa")
                        alertDialogBuilder.setMessage("Bạn có chắc chắn muốn xóa sinh viên này không?")
                        alertDialogBuilder.setPositiveButton("Xóa") { dialog, _ ->
                            // Gọi API để xóa sinh viên
                            deleteStudentFromApi(studentToDelete.id.toInt())
                            dialog.dismiss()
                        }
                        alertDialogBuilder.setNegativeButton("Hủy") { dialog, _ ->
                            dialog.dismiss()
                        }
                        alertDialogBuilder.show()
                    }
                }
            )
        )
    }

    private fun deleteStudentFromApi(studentId: Int) {
        StudentService.apiUser.deleteStudent(studentId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Xóa sinh viên thành công, cập nhật RecyclerView
                    callApi()
                } else {
                    Toast.makeText(requireContext(), "Xóa sinh viên thất bại", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Gặp lỗi khi kết nối đến máy chủ", Toast.LENGTH_SHORT).show()
            }
        })
    }

//chức năng thêm

    //chuc nang chinh sua
    private fun showEditStudentDialog(selectedStudent: Student) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_student, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.et_name)
        val addressEditText = dialogView.findViewById<EditText>(R.id.et_address)
        val dobEditText = dialogView.findViewById<EditText>(R.id.et_dayOfBirth)

        // Hiển thị thông tin của item được chọn lên các EditText
        nameEditText.setText(selectedStudent.name)
        addressEditText.setText(selectedStudent.address)
        dobEditText.setText(selectedStudent.dayOfBirth)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Chỉnh sửa sinh viên")
            .setView(dialogView)
            .setPositiveButton("Lưu") { dialog, _ ->
                val newName = nameEditText.text.toString()
                val newAddress = addressEditText.text.toString()
                val newDob = dobEditText.text.toString()

                // Gọi API để cập nhật thông tin sinh viên
                updateStudent(selectedStudent.id, newName, newAddress, newDob)

                dialog.dismiss()
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun updateStudent(studentId: Long, newName: String, newAddress: String, newDob: String) {
        // Tạo đối tượng Student mới với thông tin chỉnh sửa
        val updatedStudent = Student(id = studentId, name = newName, image = "2", address = newAddress, dayOfBirth = newDob)

        // Gọi API để cập nhật thông tin sinh viên
        StudentService.apiUser.updateStudent(studentId, updatedStudent).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Nếu cập nhật thành công, cập nhật RecyclerView để hiển thị lại thông tin sinh viên
                    callApi()
                } else {
                    Toast.makeText(requireContext(), "Cập nhật thông tin sinh viên thất bại", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Gặp lỗi khi kết nối đến máy chủ", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
