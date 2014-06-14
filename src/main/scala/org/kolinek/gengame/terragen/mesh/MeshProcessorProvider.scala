package org.kolinek.gengame.terragen.mesh

import org.kolinek.gengame.geometry._

trait MeshProcessorProvider {
    def meshProcessor: MeshProcessor
}

trait DefaultMeshProcessorProvider extends MeshProcessorProvider {
    lazy val meshProcessor = new BasicMeshProcessor(10.chunk, 180)
}