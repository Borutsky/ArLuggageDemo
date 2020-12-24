package com.borutsky.arluggagedemo

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment

class MainActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment

    private var boxNode: Node? = null
    private var transformableNode: DragTransformableNode? = null
    private var anchorNode: AnchorNode? = null

    private var placed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment
        initArFragment()
        initListeners()
    }

    private fun initArFragment() {
        arFragment.arSceneView.scene.addOnUpdateListener {
            arFragment.arSceneView?.let { sceneView ->
                sceneView.arFrame?.let { frame ->
                    if (frame.camera.trackingState == TrackingState.TRACKING) {
                        val hitTest =
                                frame.hitTest(sceneView.width / 2f, sceneView.height / 2f)
                        val hitTestIterator = hitTest.iterator()
                        if (hitTestIterator.hasNext()) {
                            if (!placed) {
                                val hitResult = hitTestIterator.next()
                                val anchor = hitResult.createAnchor()
                                if (anchorNode == null) {
                                    anchorNode = AnchorNode()
                                    anchorNode?.setParent(sceneView.scene)
                                    transformableNode = DragTransformableNode(arFragment.transformationSystem)
                                    transformableNode?.setParent(anchorNode)
                                    boxNode = createBoxNode(.4f, .6f, .4f)
                                    boxNode?.setParent(transformableNode)
                                }
                                anchorNode?.anchor?.detach()
                                anchorNode?.anchor = anchor
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initListeners() {
        findViewById<Button>(R.id.buttonPlace).setOnClickListener {
            placed = !placed
            if (placed) {
                createLines(.4f, .6f, .4f, .04f, boxNode)
            } else {
                removeLines(transformableNode)
            }
        }
    }

    private fun createBoxNode(width: Float, height: Float, length: Float): Node {
        val node = Node()
        val color = com.google.ar.sceneform.rendering.Color(Color.RED)
        color.a = 0f
        MaterialFactory.makeTransparentWithColor(
                applicationContext,
                color
        )
                .thenAccept { material ->
                    val vectorCenter = Vector3.zero()
                    vectorCenter.y += 0.3f
                    val model = ShapeFactory.makeCube(
                            Vector3(width, height, length),
                            vectorCenter,
                            material
                    )
                    model.isShadowReceiver = false
                    model.isShadowCaster = false
                    node.renderable = model
                }
        return node
    }

    private fun createLines(width: Float, height: Float, length: Float, offset: Float, boxNode: Node?) {
        boxNode?.let { node ->
            val p = node.localPosition
            val hW = width / 2f
            val hL = length / 2f
            val lfbCorner = Vector3(
                    p.x - hW,
                    p.y,
                    p.z - hL
            )
            val rfbCorner = Vector3(
                    p.x + hW,
                    p.y,
                    p.z - hL
            )
            val lrbCorner = Vector3(
                    p.x - hW,
                    p.y,
                    p.z + hL
            )
            val rrbCorner = Vector3(
                    p.x + hW,
                    p.y,
                    p.z + hL
            )
            val lftCorner = Vector3(
                    p.x - hW,
                    p.y + height,
                    p.z - hL
            )
            val rftCorner = Vector3(
                    p.x + hW,
                    p.y + height,
                    p.z - hL
            )
            val lrtCorner = Vector3(
                    p.x - hW,
                    p.y + height,
                    p.z + hL
            )
            val rrtCorner = Vector3(
                    p.x + hW,
                    p.y + height,
                    p.z + hL
            )
            val lfbCornerWithOffset = Vector3(
                    lfbCorner.x - offset,
                    lfbCorner.y,
                    lfbCorner.z - offset
            )
            val lftCornerWithOffset = Vector3(
                    lftCorner.x - offset,
                    lftCorner.y,
                    lftCorner.z - offset
            )
            val rfbCornerWithOffset = Vector3(
                    rfbCorner.x,
                    rfbCorner.y,
                    rfbCorner.z - offset
            )
            val lrbCornerWithOffset = Vector3(
                    lrbCorner.x - offset,
                    lrbCorner.y,
                    lrbCorner.z
            )
            lineBetweenPoints(lfbCorner, rfbCorner, .003f, "fb", Color.RED).setParent(
                    node.parent
            )
            lineBetweenPoints(lfbCorner, lrbCorner, .003f, "lb", Color.RED).setParent(
                    node.parent
            )
            lineBetweenPoints(lrbCorner, rrbCorner, .003f, "reb", Color.RED).setParent(
                    node.parent
            )
            lineBetweenPoints(rfbCorner, rrbCorner, .003f, "rb", Color.RED).setParent(
                    node.parent
            )
            lineBetweenPoints(lfbCorner, lftCorner, .003f, "lfs", Color.RED).setParent(
                    node.parent
            )
            lineBetweenPoints(rfbCorner, rftCorner, .003f, "rfs", Color.RED).setParent(
                    node.parent
            )
            lineBetweenPoints(lrbCorner, lrtCorner, .003f, "lrs", Color.RED).setParent(
                    node.parent
            )
            lineBetweenPoints(rrbCorner, rrtCorner, .003f, "rrs", Color.RED).setParent(
                    node.parent
            )
            lineBetweenPoints(lftCorner, rftCorner, .003f, "ft", Color.RED).setParent(
                    node.parent
            )
            lineBetweenPoints(lftCorner, lrtCorner, .003f, "lt", Color.RED).setParent(
                    node.parent
            )
            lineBetweenPoints(lrtCorner, rrtCorner, .003f, "ret", Color.RED).setParent(
                    node.parent
            )
            lineBetweenPoints(rftCorner, rrtCorner, .003f, "rt", Color.RED).setParent(
                    node.parent
            )
            val guideLineSide = lineBetweenPoints(
                    lfbCornerWithOffset,
                    lftCornerWithOffset,
                    .005f,
                    "gls",
                    Color.WHITE
            )
            guideLineSide.setParent(node.parent)
            val guideLineLeft = lineBetweenPoints(
                    lfbCornerWithOffset,
                    lrbCornerWithOffset,
                    .005f,
                    "glb",
                    Color.WHITE
            )
            guideLineLeft.setParent(node.parent)
            val guideLineFront = lineBetweenPoints(
                    lfbCornerWithOffset,
                    rfbCornerWithOffset,
                    .005f,
                    "gfb",
                    Color.WHITE
            )
            guideLineFront.setParent(node.parent)
            val textNodeFront = Node()
            textNodeFront.name = "textFront"
            ViewRenderable.builder()
                    .setView(this, R.layout.test_view)
                    .build()
                    .thenAccept { renderable ->
                        renderable.isShadowCaster = false
                        renderable.isShadowReceiver = false
                        textNodeFront.renderable = renderable
                    }
            textNodeFront.setParent(node.parent)
            textNodeFront.localPosition = Vector3(
                    (lfbCorner.x + rfbCorner.x) / 2f,
                    lfbCorner.y,
                    lfbCorner.z - offset
            )
            val textNodeLeft = Node()
            textNodeLeft.name = "textLeft"
            ViewRenderable.builder()
                    .setView(this, R.layout.test_view)
                    .build()
                    .thenAccept { renderable ->
                        renderable.isShadowCaster = false
                        renderable.isShadowReceiver = false
                        textNodeLeft.renderable = renderable
                    }
            textNodeLeft.setParent(node.parent)
            textNodeLeft.localPosition = Vector3(
                    lfbCorner.x - offset,
                    lfbCorner.y,
                    (lfbCorner.z + lrbCorner.z) / 2f
            )
            textNodeLeft.localRotation = guideLineFront.localRotation
            val textNodeSide = Node()
            textNodeSide.name = "textLeft"
            ViewRenderable.builder()
                    .setView(this, R.layout.test_view)
                    .build()
                    .thenAccept { renderable ->
                        renderable.isShadowCaster = false
                        renderable.isShadowReceiver = false
                        textNodeSide.renderable = renderable
                    }
            textNodeSide.setParent(node.parent)
            textNodeSide.localPosition = Vector3(
                    lfbCornerWithOffset.x,
                    (lfbCorner.y + lftCorner.y) / 2f,
                    lfbCornerWithOffset.z
            )
            textNodeSide.localRotation = Quaternion.axisAngle(Vector3(0f, 0f, 1f), 270f)
        }
    }

    private fun removeLines(parent: Node?) {
        val toDelete =
                parent?.children?.filter { it.name.contains("line") || it.name.contains("text") }
        toDelete?.forEach { parent?.removeChild(it) }
    }

    private fun lineBetweenPoints(
            point1: Vector3,
            point2: Vector3,
            thickness: Float,
            name: String,
            color: Int
    ): Node {
        val lineNode = Node()
        lineNode.name = "line$name"
        var lineRenderable: ModelRenderable?
        val difference = Vector3.subtract(point1, point2)
        val directionFromTopToBottom = difference.normalized()
        val rotationFromAToB = Quaternion.lookRotation(directionFromTopToBottom, Vector3.up())
        MaterialFactory.makeOpaqueWithColor(
                this,
                com.google.ar.sceneform.rendering.Color(color)
        ).thenAccept { material: Material? ->
            lineRenderable = ShapeFactory.makeCube(
                    Vector3(thickness, thickness, difference.length()),
                    Vector3.zero(), material
            )
            lineRenderable?.isShadowCaster = false
            lineRenderable?.isShadowReceiver = false
            lineNode.renderable = lineRenderable
        }
        return lineNode.apply {
            localPosition = Vector3.add(point1, point2).scaled(.5f)
            localRotation = rotationFromAToB
        }
    }

}