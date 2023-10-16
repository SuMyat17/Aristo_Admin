package com.aristo.admin.view.Categories.AddProducts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.R
import com.aristo.admin.databinding.CategoryDeleteConfirmationDialogBinding

class DeleteMainCategoryDialogFragment : DialogFragment() {

    var id : String? = null
    private lateinit var binding : CategoryDeleteConfirmationDialogBinding

    private var listener: OnFinishDeleteMain? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        id = arguments?.getString("main_cat_id")

        binding = CategoryDeleteConfirmationDialogBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            dialog!!.cancel()

        }

        binding.btnConfirm.setOnClickListener {

            binding.deleteProgress.visibility = View.VISIBLE

            id?.let { it1 ->
                CategoryFirebase.deleteMainCategory(it1) { isSuccess, message ->

                    binding.deleteProgress.visibility = View.GONE

                    if (isSuccess){
                        if (message != null) {
                            listener?.onFinishActivity(message)
                        }

                        dialog!!.cancel()
                    }
                    else{
                        listener?.onFinishActivity("Failed to delete.")

                        dialog!!.cancel()
                    }

                    //Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as OnFinishDeleteMain
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement MyDialogListener")
        }
    }


}