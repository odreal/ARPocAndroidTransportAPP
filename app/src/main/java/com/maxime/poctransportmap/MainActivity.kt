//Code based on the following sample : https://github.com/cgathergood/Your-First-AR-App-with-Sceneform/tree/master/app
package com.maxime.poctransportmap


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Point
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.math.Quaternion




class MainActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment

    private var isTracking: Boolean = false
    private var isHitting: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = sceneform_fragment as ArFragment

        arFragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            arFragment.onUpdate(frameTime)
            onUpdate()
        }

        floatingActionButton.setOnClickListener {
            addObject(Uri.parse("lowpolyParisLoosesQuality.sfb"), true, "map")
            addObject(Uri.parse("lowpolyParisLine1Edited.sfb"), false, "line")
            addObject(Uri.parse("lowpolyParisLine4Edited.sfb"), false, "line")
            addObject(Uri.parse("lowpolyParisLine11Edited.sfb"), false, "line")
            addObject(Uri.parse("lowpolyParisLine14Edited.sfb"), false, "line")
            addObject(Uri.parse("lowpolyParisStationsEdited.sfb"), true, "station")
        }
        placeIsPossible(false)

    }

    private fun placeIsPossible(enabled: Boolean) {
        if (enabled) {
            floatingActionButton.isEnabled = true
            floatingActionButton.visibility = View.VISIBLE
        } else {
            floatingActionButton.isEnabled = false
            floatingActionButton.visibility = View.GONE
        }
    }

    private fun onUpdate() {
        updateTracking()
        if (isTracking) {
            val hitTestChanged = updateHitTest()
            if (hitTestChanged) {
                placeIsPossible(isHitting)
            }
        }

    }

    private fun updateHitTest(): Boolean {
        val frame = arFragment.arSceneView.arFrame
        val point = getScreenCenter()
        val hits: List<HitResult>
        val wasHitting = isHitting
        isHitting = false
        if (frame != null) {
            hits = frame.hitTest(point.x.toFloat(), point.y.toFloat())
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                    isHitting = true
                    break
                }
            }
        }
        return wasHitting != isHitting
    }

    private fun updateTracking(): Boolean {
        val frame = arFragment.arSceneView.arFrame
        val wasTracking = isTracking
        isTracking = frame!!.camera.trackingState == TrackingState.TRACKING
        return isTracking != wasTracking
    }

    private fun getScreenCenter(): Point {
        val view = findViewById<View>(android.R.id.content)
        return Point(view.width / 2, view.height / 2)
    }

    private fun addObject(model: Uri, needReplace: Boolean, replaceType: String) {
        val frame = arFragment.arSceneView.arFrame
        val point = getScreenCenter()
        if (frame != null) {
            val hits = frame.hitTest(point.x.toFloat(), point.y.toFloat())
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                    placeObject(arFragment, hit.createAnchor(), model, needReplace, replaceType)
                    break
                }
            }
        }
    }

    private fun placeObject(fragment: ArFragment, anchor: Anchor, model: Uri, needReplace: Boolean, replaceType: String) {
        ModelRenderable.builder()
            .setSource(fragment.context, model)
            .build()
            .thenAccept {
                addNodeToScene(fragment, anchor, it, needReplace, replaceType)
            }
            .exceptionally {
                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                return@exceptionally null
            }
    }

    private fun addNodeToScene(fragment: ArFragment, anchor: Anchor, renderable: ModelRenderable, needReplace: Boolean, replaceType: String) {
        val anchorNode = AnchorNode(anchor)
        val transformableNode = TransformableNode(fragment.transformationSystem)
        transformableNode.scaleController.minScale = 0.01f
        transformableNode.scaleController.maxScale = 0.1f
        val numAnimations = renderable.animationDataCount
        Log.e("KEBAB", numAnimations.toString())
        if(numAnimations > 0){
            val mainAnim = renderable.getAnimationData("Tram|Tram")
            val renderableAnimator = ModelAnimator(mainAnim, renderable)
            renderableAnimator.start()
            renderableAnimator.repeatCount = 9999

        }

        if(needReplace){
            if(replaceType == "map") {
                transformableNode.localPosition = Vector3(0.025f, -0.05f, -0.075f)
                transformableNode.localRotation = Quaternion(Vector3(0f,180f,0f))
            } else if(replaceType == "station"){
                transformableNode.localRotation = Quaternion(Vector3(0f,180f,0f))
            }

        }



        transformableNode.renderable = renderable
        transformableNode.setParent(anchorNode)
        fragment.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()
    }
}