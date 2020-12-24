package com.borutsky.arluggagedemo

import com.borutsky.arluggagedemo.DragRotationController
import com.google.ar.sceneform.ux.TransformableNode
import com.google.ar.sceneform.ux.TransformationSystem

class DragTransformableNode(transformationSystem: TransformationSystem) :
    TransformableNode(transformationSystem) {

    private val dragRotationController = DragRotationController(
        this,
        transformationSystem.dragRecognizer
    )

    init {
        translationController.isEnabled = false
        removeTransformationController(translationController)
        removeTransformationController(rotationController)
        addTransformationController(dragRotationController)
    }
}