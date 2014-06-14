package org.kolinek.gengame.terragen

import org.kolinek.gengame.terragen.mesh.NormalCalculator
import org.kolinek.gengame.geometry._
import com.jme3.scene.Mesh
import com.jme3.math.Vector3f
import spire.syntax.all._
import com.jme3.scene.VertexBuffer
import com.jme3.util.BufferUtils
import com.jme3.scene.Geometry
import com.jme3.material.Material
import com.jme3.asset.AssetManager
import com.jme3.material.RenderState.FaceCullMode

class TerrainPiece(val points: IndexedSeq[Position], val indexes: Seq[Int], val normals: Seq[Position]) {
    def this(points: IndexedSeq[Position], indexes: Seq[Int]) =
        this(points, indexes, NormalCalculator.createNormals(points, indexes))

    def toJmeGeometry(assetManager: AssetManager) = {
        val mesh = new Mesh;
        val posBuffer = points.map(x => new Vector3f(x.x.toFloat, x.y.toFloat, x.z.toFloat)).toArray[Vector3f]
        val indexBuffer = indexes.toArray[Int]
        val normalBuffer = normals.map(x => new Vector3f(x.x.toFloat, x.y.toFloat, x.z.toFloat)).toArray
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(posBuffer: _*))
        mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indexBuffer: _*))
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normalBuffer: _*))
        mesh.setBound(BoundingBox.fromPoints(points).toJmeBoundingBox)
        val geom = new Geometry("terrainpiece", mesh)
        val material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        material.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        geom.setMaterial(material)
        geom
    }
}
