package com.example.madcamp_1st

import ai.fritz.core.Fritz
import ai.fritz.stylepaintingmodels.PaintingStyles
import ai.fritz.stylepaintingmodels.PatternStyles
import ai.fritz.vision.*
import ai.fritz.vision.styletransfer.FritzVisionStylePredictor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ai.fritz.vision.styletransfer.FritzVisionStyleResult
import ai.fritz.vision.styletransfer.PaintingManagedModels
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.util.Size
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.custom.*
import androidx.camera.core.*
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getMainExecutor
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executor


class CustomFragment : Fragment() {

    private lateinit var mainExecutor: Executor

    var predictor: FritzVisionStylePredictor? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.custom, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainExecutor = getMainExecutor(requireContext())

        view_finder.post {
            startCamera()
        }

    }

    private fun startCamera() {
        //Specify the configuration for the preview
        val previewConfig = PreviewConfig.Builder()
            .apply {
                //Set the resolution of the captured image
                setTargetResolution(Size(1920, 1080))
            }
            .build()

        //Generate a preview
        val preview = Preview(previewConfig)

        //Add a listener to update preview automatically
        preview.setOnPreviewOutputUpdateListener {

            val parent = view_finder.parent as ViewGroup

            //Remove thr old preview
            parent.removeView(view_finder)

            //Add the new preview
            parent.addView(view_finder, 0)
            view_finder.surfaceTexture = it.surfaceTexture
        }

        val analyzerConfig = ImageAnalysisConfig.Builder().apply {
            setImageReaderMode(
                ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE
            )
            setTargetResolution(Size(1920, 1080))
        }.build()

        val imageAnalysis = ImageAnalysis(analyzerConfig).apply {
            setAnalyzer(mainExecutor, ImageProcessor(requireActivity(), custom_image_view))
        }

        CameraX.bindToLifecycle(this, preview, imageAnalysis)
    }

}

class ImageProcessor(val activity: FragmentActivity, val image_view: ImageView) : ImageAnalysis.Analyzer {
    var predictor: FritzVisionStylePredictor? = null
    val TAG = javaClass.simpleName

    override fun analyze(image: ImageProxy?, rotationDegrees: Int) {

        //Handle all the ML logic here
        val mediaImage = image?.image

        val imageRotation = ImageRotation.getFromValue(rotationDegrees)

        val visionImage = FritzVisionImage.fromMediaImage(mediaImage, imageRotation)

        val managedModel = PaintingManagedModels.BICENTENNIAL_PRINT_MANAGED_MODEL

        /* Load the FritzVision Style Transfer Predictor
         */
        FritzVision.StyleTransfer.loadPredictor(
            managedModel, object : PredictorStatusListener<FritzVisionStylePredictor> {
                override fun onPredictorReady(stylePredictor: FritzVisionStylePredictor?) {
                    Log.d(TAG, "Style Transfer predictor is ready")
                    predictor = stylePredictor
                }
            }
        )
        /*
         *  !!! SET STYLES HERE !!!
         *  reference below
         *  https://docs.fritz.ai/develop/vision/style-transfer/android.html#
         */
        val styleOnDeviceModel = PaintingStyles.STARRY_NIGHT
        predictor = FritzVision.StyleTransfer.getPredictor(styleOnDeviceModel)


        /* Get the FritzVisionStyleResult by running the predictor on the vision image.
         */
        val styleResult = predictor?.predict(visionImage)

        /* Perform UI operations accordingly.
         */
        activity.runOnUiThread {
            Log.d(TAG, "UI thread")
            val bitmap = styleResult?.toBitmap()
            image_view.setImageBitmap(bitmap)
        }
    }
}