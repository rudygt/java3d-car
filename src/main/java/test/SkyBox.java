package test;

import org.scijava.java3d.utils.geometry.*;

import org.scijava.java3d.*;
import org.scijava.vecmath.*;

/*******************************************************************************
 * This class can be used to create a SkyBox to hold the background image. It
 * creates this SkyBox together with the required texture coordinates. A texture
 * is expected which contains six tiles right beside another. These tiles have
 * to contain the following images for the faces of the SkyBox: left, front,
 * right, back, top and bottom (from left to right). Different to other 3D
 * objects here the texture coordinates are created in every case.
 */
public class SkyBox extends Shape3D
   {
   public static final int GENERATE_NORMALS=Box.GENERATE_NORMALS;
   public static final int GENERATE_NORMALS_INWARD=Box.GENERATE_NORMALS_INWARD;
   public static final int ENABLE_APPEARANCE_MODIFY=Box.ENABLE_APPEARANCE_MODIFY;



  /*****************************************************************************
   * The default constructor. It creates a 1x1x1 m white SkyBox.
   */
   public SkyBox()
      {
      this(1.0f,SkyBox.GENERATE_NORMALS,new Appearance());
      }



  /*****************************************************************************
   * SkyBox constructor
   * @param dim width of the SkyBox
   * @param ap Appearance of the SkyBox which has to be generated
   */
   public SkyBox(float dim, Appearance ap)
      {
      this(dim,SkyBox.GENERATE_NORMALS,ap);
      }



  /*****************************************************************************
   * SkyBox constructor
   * @param dim width of the SkyBox
   * @param flags specifies some properties of the SkyBox object, for more
   *        information please refer the flag values taken from @Box
   * @param ap Appearance of the SkyBox which has to be generated
   */
   public SkyBox(float dim,int flags,Appearance ap)
      {
      Point3f[]        CoordArr=new Point3f[24];
      GeometryInfo     MyGInf;
      GeometryArray    GArr;
      final float      TOP_CORR=0f,BOT_CORR=1f;

      CoordArr[1]=new Point3f(-dim,dim,dim);   CoordArr[0]=new Point3f(-dim,dim,-dim);
      CoordArr[3]=new Point3f(-dim,-dim,-dim); CoordArr[2]=new Point3f(-dim,-dim,dim);

      CoordArr[5]=new Point3f(-dim,dim,-dim);  CoordArr[4]=new Point3f(dim,dim,-dim);
      CoordArr[7]=new Point3f(dim,-dim,-dim);  CoordArr[6]=new Point3f(-dim,-dim,-dim);

      CoordArr[9]=new Point3f(dim,dim,-dim);   CoordArr[8]=new Point3f(dim,dim,dim);
      CoordArr[11]=new Point3f(dim,-dim,dim);  CoordArr[10]=new Point3f(dim,-dim,-dim);

      CoordArr[13]=new Point3f(dim,dim,dim);   CoordArr[12]=new Point3f(-dim,dim,dim);
      CoordArr[15]=new Point3f(-dim,-dim,dim); CoordArr[14]=new Point3f(dim,-dim,dim);

      CoordArr[17]=new Point3f(-dim,dim,dim);  CoordArr[16]=new Point3f(dim,dim,dim);
      CoordArr[19]=new Point3f(dim,dim,-dim);  CoordArr[18]=new Point3f(-dim,dim,-dim);

      CoordArr[21]=new Point3f(-dim,-dim,dim);  CoordArr[20]=new Point3f(-dim,-dim,-dim);
      CoordArr[23]=new Point3f(dim,-dim,-dim);  CoordArr[22]=new Point3f(dim,-dim,dim);

      MyGInf=new GeometryInfo(GeometryInfo.QUAD_ARRAY);
      MyGInf.setTextureCoordinateParams(1,2);
      TexCoord2f[] TCoords=new TexCoord2f[24];

      TCoords[2]=new TexCoord2f( 0f , TOP_CORR);     TCoords[3]=new TexCoord2f(0.1666666f,TOP_CORR);
      TCoords[0]=new TexCoord2f(0.1666666f,BOT_CORR);  TCoords[1]=new TexCoord2f( 0f,BOT_CORR);

      TCoords[6]=new TexCoord2f(0.1666666f,TOP_CORR);  TCoords[7]=new TexCoord2f(0.3333333f,TOP_CORR);
      TCoords[4]=new TexCoord2f(0.3333333f,BOT_CORR);  TCoords[5]=new TexCoord2f(0.1666666f,BOT_CORR);

      TCoords[10]=new TexCoord2f(0.3333333f,TOP_CORR);  TCoords[11]=new TexCoord2f(0.5f,TOP_CORR);
      TCoords[8]=new TexCoord2f(0.5f,BOT_CORR);       TCoords[9]=new TexCoord2f(0.3333333f,BOT_CORR);

      TCoords[14]=new TexCoord2f(0.499f,TOP_CORR);       TCoords[15]=new TexCoord2f(0.6666f,TOP_CORR);
      TCoords[12]=new TexCoord2f(0.6666f,BOT_CORR);    TCoords[13]=new TexCoord2f(0.499f,BOT_CORR);

      TCoords[18]=new TexCoord2f( 5f / 6f,BOT_CORR);    TCoords[19]=new TexCoord2f( 4f / 6f,BOT_CORR);
      TCoords[16]=new TexCoord2f( 4f / 6f,TOP_CORR);    TCoords[17]=new TexCoord2f( 5f / 6f ,TOP_CORR);

      TCoords[20]=new TexCoord2f(0.835f,BOT_CORR);    TCoords[21]=new TexCoord2f(0.835f,TOP_CORR);
      TCoords[22]=new TexCoord2f(1,TOP_CORR);          TCoords[23]=new TexCoord2f(1,BOT_CORR);

      MyGInf.setTextureCoordinates(0,TCoords);

      MyGInf.setCoordinates(CoordArr);

      if (((flags & SkyBox.GENERATE_NORMALS)!=0) || ((flags & SkyBox.GENERATE_NORMALS_INWARD)!=0))
       (new NormalGenerator()).generateNormals(MyGInf);
      MyGInf.compact();
      GArr=MyGInf.getGeometryArray();

      if ((flags & ENABLE_PICK_REPORTING)!=0) GArr.setCapability(GeometryArray.ALLOW_INTERSECT);

      this.setGeometry(GArr);

      if ((flags & SkyBox.ENABLE_APPEARANCE_MODIFY)!=0)
         {
         this.setCapability(SkyBox.ALLOW_APPEARANCE_READ);
         this.setCapability(SkyBox.ALLOW_APPEARANCE_WRITE);
         }
      this.setAppearance(ap);
      }

   }
