package com.hexa.arti.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import com.hexa.arti.R
import com.hexa.arti.util.setFullScreen
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Size
import io.github.sceneview.node.ImageNode
import kotlinx.coroutines.launch

class ARActivity : AppCompatActivity(R.layout.activity_aractivity) {

    lateinit var sceneView: ARSceneView
    lateinit var loadingView: View
    var imageUrl: String? = ""

    var isLoading = false
        set(value) {
            field = value
            loadingView.isGone = !value
        }

    var anchorNode: AnchorNode? = null
        set(value) {
            if (field != value) {
                field = value
            }
        }

    var anchorNodeView: View? = null

    var trackingFailureReason: TrackingFailureReason? = null
        set(value) {
            if (field != value) {
                field = value
            }
        }

    var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageUrl = intent.getStringExtra("image")

        imageUrl?.let {
            Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        imageBitmap = resource
//                        findViewById<ImageView>(R.id.iv_test).setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        setFullScreen(
            findViewById(R.id.rootView),
            fullScreen = true,
            hideSystemBars = false,
            fitsSystemWindows = false
        )

        loadingView = findViewById(R.id.loadingView)
        sceneView = findViewById<ARSceneView?>(R.id.sceneView).apply {
            lifecycle = this@ARActivity.lifecycle
            planeRenderer.isEnabled = true

            configureSession { session, config ->
                config.depthMode = when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                    true -> Config.DepthMode.AUTOMATIC
                    else -> Config.DepthMode.DISABLED
                }



                config.focusMode = Config.FocusMode.AUTO
                config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL
//                config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE


                config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            }

            onSessionUpdated = { _, frame ->
                if (anchorNode == null) {
                    frame.getUpdatedPlanes()
                        .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                        ?.let { plane ->
                            addAnchorNode(plane.createAnchor(plane.centerPose))
                        }
                }
            }
            onTrackingFailureChanged = { reason ->
                this@ARActivity.trackingFailureReason = reason
            }

//            setOnTouchListener { view, event ->
//                if (event.action == MotionEvent.ACTION_DOWN) {
//                    val session = sceneView.session
//                    session?.let {
//                        val frame: Frame = session.update()
//                        val hitResults = frame.hitTest(event)
//                        for (hit in hitResults) {
//                            val trackable = hit.trackable
//                            if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
//                                addAnchorNode(hit.createAnchor())
//                                break
//                            }
//                        }
//                    }
//                    view.performClick()
//                }
//                true
//            }
            planeRenderer.isEnabled = false

        }


    }

    private fun addAnchorNode(anchor: Anchor) {
        sceneView.addChildNode(
            AnchorNode(sceneView.engine, anchor)
                .apply {
                    isEditable = true
                    lifecycleScope.launch {
                        isLoading = true
                        buildImageNode()?.let { addChildNode(it) }
                        isLoading = false
                    }
                    anchorNode = this
                }
        )
    }

    private fun buildImageNode(): ImageNode? {

        imageBitmap ?: return null

        val imageNode = ImageNode(
            materialLoader = sceneView.materialLoader,
            bitmap = imageBitmap!!,
            size = Size(0.5f, 0.5f)
        )

        imageNode.rotation = Rotation(-90f, 0f, 0f)  // x축 기준 90도 회전
        imageNode.isEditable = true
        imageNode.editableScaleRange = 0.3f..5.0f

        return imageNode
    }


}