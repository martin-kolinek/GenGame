package org.kolinek.gengame.geometry

import org.scalatest.FunSuite

class UnitsTest extends FunSuite {
    test("ChunkUnit has correct bounds") {
        assert(0.chunk.lower === 0.cube)
        assert(0.chunk.upper === 31.cube)
        assert(1.chunk.lower === 32.cube)
        assert(1.chunk.upper === 63.cube)
        assert(-1.chunk.upper === -1.cube)
        assert(-1.chunk.lower === -32.cube)
        assert(-2.chunk.lower === -64.cube)
        assert(-2.chunk.upper === -33.cube)
        assert(13.chunk.lower === 416.cube)
        assert(13.chunk.upper === 447.cube)

        assert(0.cube.bound === 0.chunk)
        assert(5.cube.bound === 0.chunk)
        assert(31.cube.bound === 0.chunk)
        assert(32.cube.bound === 1.chunk)
        assert(34.cube.bound === 1.chunk)
        assert(-1.cube.bound === -1.chunk)
        assert(-32.cube.bound === -1.chunk)
        assert(-33.cube.bound === -2.chunk)
        assert(-64.cube.bound === -2.chunk)
    }

    test("CubeUnit has correct bounds") {
        assert(0.cube.lower === 0.pos)
        assert(0.cube.upper === 1.pos)
        assert(1.cube.lower === 1.pos)
        assert(1.cube.upper === 2.pos)
        assert(-1.cube.upper === -0.pos)
        assert(-1.cube.lower === -1.pos)
        assert(15.cube.lower === 15.pos)
        assert(15.cube.upper === 16.pos)
        
        assert(0.pos.bound === 0.cube)
        assert(0.99999.pos.bound === 0.cube)
        assert(1.0.pos.bound === 1.cube)
        assert(1.5.pos.bound === 1.cube)
        assert(2.0.pos.bound === 2.cube)
        assert(15.999999.pos.bound === 15.cube)
        assert(16.pos.bound === 16.cube)
        assert(-0.0000001.pos.bound === -1.cube)
        assert(-1.pos.bound === -1.cube)
        assert(-1.5.pos.bound === -2.cube)
        assert(-1.9999999.pos.bound === -2.cube)
        assert(-2.pos.bound === -2.cube)
    }
}