package com.saikauskas.julius.arpracticeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

enum Levels {
    POTATO, BANANA, BOOM;
}
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "log";
    private ArFragment arFragment;
    private ModelRenderable modelRenderable;
    AnchorNode anchorNode;
    //TransformableNode node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        Levels levels = Levels.BANANA;

        Log.d(TAG, "onCreate: flavor is banana");

        setupModel();
        setupPlane();



    }


    private void setupModel() {

        ModelRenderable.builder()
                .setSource(this, R.raw.model)
                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Error couldn't load model.", Toast.LENGTH_SHORT).show();
                    return null;
                });

    }

    private void setupPlane() {

        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                Anchor anchor = hitResult.createAnchor();
                anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());
                createModel(anchorNode);

            }

        });

    }


    private void createModel(AnchorNode anchorNode){

        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(modelRenderable);
        node.select();

        node.setOnTapListener(new Node.OnTapListener() {
            @Override
            public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {
                removeAnchorNode(anchorNode);
            }
        });
        
    }

    private void removeAnchorNode(AnchorNode nodeToremove) {
        //Remove an anchor node
        if (nodeToremove != null) {
            arFragment.getArSceneView().getScene().removeChild(nodeToremove);
            nodeToremove.getAnchor().detach();
            nodeToremove.setParent(null);

        }
    }

}
