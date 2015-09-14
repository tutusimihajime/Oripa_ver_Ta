/**
 * ORIPA - Origami Pattern Editor 
 * Copyright (C) 2005-2009 Jun Mitani http://mitani.cs.tsukuba.ac.jp/

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package oripa;

import java.io.BufferedWriter;
import java.io.FileWriter;
import oripa.geom.OriEdge;
import oripa.geom.OriFace;
import oripa.geom.OriHalfedge;
import oripa.geom.OriVertex;

//2015 9/3 0:59
import oripa.geom.SubFace;
import java.util.ArrayList;

// export folded model
public class ExporterOBJ2 {

    public static void export_bk(Doc doc, String filepath) throws Exception {
        FileWriter fw = new FileWriter(filepath);
        BufferedWriter bw = new BufferedWriter(fw);

        // Align the center of the model, combine scales
        bw.write("# Created by ORIPA\n");
        bw.write("\n");

        int id = 1;
        for (OriVertex vertex : doc.vertices) {
            bw.write("v " + vertex.p.x + " " + vertex.p.y + " 0.0\n");
            vertex.tmpInt = id;
            id++;
        }

        for (OriVertex vertex : doc.vertices) {
            bw.write("vt " + (vertex.preP.x + doc.size / 2) / doc.size + " " + (vertex.preP.y + doc.size / 2) / doc.size + "\n");
        }

        for (OriFace face : ORIPA.doc.faces) {
            bw.write("f");
            for (OriHalfedge he : face.halfedges) {
                bw.write(" " + he.vertex.tmpInt + "/" + he.vertex.tmpInt);
            }
            bw.write("\n");
        }

        bw.close();
    }
    // ↓この関数がOBJのエクスポートで呼ばれる 9/2 21:48
    public static void export(Doc doc, String filepath) throws Exception {
        FileWriter fw = new FileWriter(filepath);
        BufferedWriter bw = new BufferedWriter(fw);

        // Align the center of the model, combine scales
        bw.write("# Created by ORIPA \n");
        bw.write("\n");

        int id = 1;
        for (OriVertex vertex : doc.vertices) {
            bw.write("v " + vertex.p.x + " " + vertex.p.y + " 0.0\n");
            vertex.tmpInt = id;
            id++;
        }

        for (OriFace face : ORIPA.doc.faces) {
            bw.write("f");
            for (OriHalfedge he : face.halfedges) {
                bw.write(" " + he.vertex.tmpInt);
            }
            bw.write("\n");
        }

        for (OriEdge edge : ORIPA.doc.edges) {
            bw.write("e " + edge.sv.tmpInt + " " + edge.ev.tmpInt + " " + edge.type + " 180\n");
        }
        
        

        bw.close();
    }
    // ↓こっちを呼ぶようにします 9/3 0:54
    public static void export_tanaka(Doc doc, String filepath) throws Exception {
        FileWriter fw = new FileWriter(filepath);
        BufferedWriter bw = new BufferedWriter(fw);

        // Align the center of the model, combine scales
        bw.write("# Created by ORIPA \n");
        bw.write("\n");

        int id = 1;
        //頂点は重複させる（Faceを分離させるため）
        ArrayList<ArrayList> indexs = new ArrayList<>(); 
        for(SubFace subface : ORIPA.doc.m_folder.subFaces){
            ArrayList<Integer> index = new ArrayList<>();
            for(OriHalfedge he : subface.outline.halfedges){
                OriVertex v = he.vertex;
                bw.write("v " + v.p.x + " " + v.p.y + " 0.0"+"\n");
                index.add(id++);
            }
            indexs.add(index);
        }
        for(int i = 0 ; i<indexs.size(); ++i){
            bw.write("f");
            for(int j=0; j<indexs.get(i).size(); ++j){
                bw.write(" "+indexs.get(i).get(j));
            }
            bw.write("\n");
        }
        //多分↓これはあってる（騙し船とかによる検証）
        for(SubFace subface : ORIPA.doc.m_folder.subFaces){
            bw.write("n "+subface.sortedFaces.size()+"\n");
        }

        bw.close();
    }
    // ↓これを呼びます 9/3 14:56
    public static void export_tanaka2(Doc doc, String filepath) throws Exception {
        FileWriter fw = new FileWriter(filepath);
        BufferedWriter bw = new BufferedWriter(fw);

        // Align the center of the model, combine scales
        bw.write("# Created by ORIPA \n");
        bw.write("\n");

        int id = 1;
        //頂点は重複させる（Faceを分離させるため）
        //SubFaces subface.faces のfaceを全部書き出す
        ArrayList<ArrayList> indexs = new ArrayList<>(); 
        for(SubFace subface : ORIPA.doc.m_folder.subFaces){
            for(OriFace f : subface.faces){
                ArrayList<Integer> index = new ArrayList();
                for(OriHalfedge he : f.halfedges){
                    OriVertex v = he.vertex;
                    bw.write("v " + v.p.x + " " + v.p.y + " 0.0"+"\n");
                    index.add(id++);
                }
                indexs.add(index);
            }
        }
        for(int i = 0 ; i<indexs.size(); ++i){
            bw.write("f");
            for(int j=0; j<indexs.get(i).size(); ++j){
                bw.write(" "+indexs.get(i).get(j));
            }
            bw.write("\n");
        }
        //多分↓これはあってる（騙し船とかによる検証）
        for(SubFace subface : ORIPA.doc.m_folder.subFaces){
            bw.write("n "+subface.sortedFaces.size()+"\n");
        }

        bw.close();
    }
    // ↓これにする 9/3 15:42
    public static void export_tanaka3(Doc doc, String filepath) throws Exception {
        FileWriter fw = new FileWriter(filepath);
        BufferedWriter bw = new BufferedWriter(fw);

        // Align the center of the model, combine scales
        bw.write("# Created by ORIPA \n");
        bw.write("\n");

        int id = 1;
        //頂点は重複させる（Faceを分離させるため）
        //SubFaces subface.faces.get(0)出力
        ArrayList<ArrayList> indexs = new ArrayList<>(); 
        for(SubFace subface : ORIPA.doc.m_folder.subFaces){
            int cnt = 0;
            //最もエッジの少ないfaces.get(i)の要素番号iを取得する
            for(int i = 0; i<subface.faces.size(); ++i){
                if(subface.faces.get(i).halfedges.size()<subface.faces.get(cnt).halfedges.size()){
                    cnt = i;
                }
            }
            OriFace f = subface.faces.get(cnt);
            ArrayList<Integer> index = new ArrayList();
            for(OriHalfedge he : f.halfedges){
                OriVertex v = he.vertex;
                bw.write("v " + v.p.x + " " + v.p.y + " 0.0"+"\n");
                index.add(id++);
            }
            indexs.add(index);
        }
        for(int i = 0 ; i<indexs.size(); ++i){
            bw.write("f");
            for(int j=0; j<indexs.get(i).size(); ++j){
                bw.write(" "+indexs.get(i).get(j));
            }
            bw.write("\n");
        }
        //重なり枚数を出力
        for(SubFace subface : ORIPA.doc.m_folder.subFaces){
            bw.write("n "+subface.sortedFaces.size()+"\n");
        }

        bw.close();
    }
}
