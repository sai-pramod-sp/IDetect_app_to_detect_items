package com.example.image

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.image.databinding.ActivityMainBinding
import com.example.image.databinding.FragmentHomeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    lateinit var camera: Button
    lateinit var image_view: ImageView
    private var isreadmediaaudio = false
    private var isreadmediavideo = false
    private var isreadmediaimages = false
    private var iscamera = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    val permissionRequest: MutableList<String> = ArrayList()
    var image_uri:  Uri ?= null
    var upload_uri: Uri ?= null
    private val IMAGE_CAPTURE_CODE: Int = 1001
    private lateinit var binding: FragmentHomeBinding
    private lateinit var firebaseref: DatabaseReference
    private lateinit var storageref: StorageReference
    private lateinit var imageLauncher: ActivityResultLauncher<Array<String>>





    // TODO: Rename and change types of parameters

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permission ->

            isreadmediaaudio = permission[Manifest.permission.READ_MEDIA_AUDIO] ?: isreadmediaaudio
            isreadmediavideo = permission[Manifest.permission.READ_MEDIA_VIDEO] ?: isreadmediavideo
            isreadmediaimages = permission[Manifest.permission.READ_MEDIA_IMAGES] ?: isreadmediaimages
            iscamera = permission[Manifest.permission.CAMERA] ?: iscamera

        }

        binding = FragmentHomeBinding.inflate(layoutInflater)
        firebaseref = FirebaseDatabase.getInstance().getReference("Images")

        //code for picking the image from the gallery
        val pickimage = registerForActivityResult(ActivityResultContracts.GetContent()){

            binding.image.setImageURI(it)
            if(it != null){
                upload_uri = it
            }
        }

        binding.camera.setOnClickListener {
            checkpermission()
        }

        binding.upload.setOnClickListener{
            pickimage.launch("image/*")

        }
        return binding.root
    }


    private fun checkpermission(){

        //if system os is Marshmellow or above we need to give runtime permissions

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if((ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                || (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED) ){

                permissionRequest.add(Manifest.permission.CAMERA)
                permissionRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
                permissionRequest.add(Manifest.permission.READ_MEDIA_AUDIO)
                permissionRequest.add(Manifest.permission.READ_MEDIA_VIDEO)

                permissionLauncher.launch(permissionRequest.toTypedArray())

            }else{
                opencamera()
            }
        }else{
            opencamera()
        }

    }

    private fun opencamera(){

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera")

        image_uri =
            activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //called when image was captured from the camera intent
        if(resultCode == Activity.RESULT_OK){
            binding.image.setImageURI(image_uri)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}