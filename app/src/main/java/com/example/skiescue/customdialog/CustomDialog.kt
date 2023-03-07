package com.example.skiescue.customdialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.skiescue.R
import com.example.skiescue.databinding.DialogIntialSetupBinding
import com.example.skiescue.home.viewmodel.HomeViewModel
import com.example.skiescue.model.initSharedPref
import com.example.skiescue.model.isConnected


class CustomDialog : DialogFragment() {
    private val viewModel: HomeViewModel by activityViewModels()

    lateinit var binding: DialogIntialSetupBinding
    lateinit var appContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogIntialSetupBinding.inflate(inflater, container, false)
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.home)
        isCancelable = false

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appContext = requireActivity().applicationContext
        binding.setupDialogButton.setOnClickListener {
           //1 means GPS //2 means MAPS

            // init for first
            initSharedPref(requireContext()).edit().apply() {
                putBoolean(getString(R.string.FIRST), false)
                apply()
            }
            if (!isConnected(requireContext())) {
                viewModel.selectedLocProvider(3)
            } else {
                if (binding.radioButtonGPS.isChecked) {
                    // init for GPS
                    initSharedPref(requireContext()).edit().apply() {
                        //false meaning GPS
                        putInt(getString(R.string.LOCATION), 1)
                        apply()
                    }
                } else if (binding.radioButtonMaps.isChecked) {
                    // true means MAPs
                    initSharedPref(requireContext()).edit()
                        .apply {
                            putInt(getString(R.string.LOCATION), 2)
                            apply()
                        }
                }
            }
            if (activity is DialogInterface.OnDismissListener) {
                (activity as (DialogInterface.OnDismissListener)).onDismiss(dialog);

            }
            dialog!!.dismiss()

        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window!!.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


}