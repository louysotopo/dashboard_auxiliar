package ListFigures;
//Imports
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import com.jams.lenovo.dash2.R;

import java.io.IOException;
import java.util.ArrayList;
import Figures.*;

import static android.view.MotionEvent.INVALID_POINTER_ID;

/**
 * This class Canvas defines a list of figures in a view
 * @author Edwin Saavedra
 * @version 3
 */
@SuppressLint("ViewConstructor")
public class ListFigure  extends View {
    //Class Attributes
    private int figureSelected;
    private ArrayList<Figure> myFigures;
    private Paint pencil;
    private int[] figureColour = {183, 149, 11};
    private float getPastX = 0;
    private float getPastY = 0;
    private float touchX = 0;
    private float touchY = 0;
    private float mPositionX;
    private float mPositionY;
    private float d;
    private float d_1;
    // acceptDistance is the acceptable distance to interact with the figure
    private float acceptDistance = 30;
    private float generalHeight = 0;
    private float generalWidth = 0;
    int mode=0; //Mode 1 is Resize... mode 2 is Move
    private  LinearLayout layout;
    //private float widthLayout = layout.getWidth();
    //private float heightLayout = layout.getHeight();
    //Mode Touch
    private int modeTouch = 0; //Mode 0 is Normal ... Mode 1 es zoomMode
    //PicToZoom
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private Drawable mBoard;
    private float mBoardWidth;
    private  float mBoardHeght;
    private float mScaleFactor = 1.f;
    private  Matrix matrix;
    private Bitmap mImage;
    private  int mImageWidth;
    private int mImageHeight;
    /**
     * Class Constructor
     * @param context The View*/
    public ListFigure(Context context,LinearLayout _layout){
        super(context);
        mBoard = ResourcesCompat.getDrawable(context.getResources(), R.drawable.rx_image_1,null);
        mBoardWidth = mBoard.getIntrinsicWidth();
        mBoardHeght = mBoard.getIntrinsicHeight();
        mBoard.setBounds(0,0,(int)mBoardWidth,(int)mBoardHeght);
        matrix = new Matrix();
        myFigures = new ArrayList<>();
        layout = _layout;
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestureDetector = new GestureDetector(context,listener);
        initialStyleFigure();
        invalidate();
    }//Closing the class constructor
    public void loadImage(Bitmap mImage){
      //mImage = img;
      float aspectRatio = (float) mImage.getHeight()/mImage.getWidth();
      DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
      mImageWidth = displayMetrics.widthPixels;
      mImageHeight = Math.round(mImageWidth*aspectRatio);
      this.mImage =  Bitmap.createScaledBitmap(mImage,mImageWidth,mImageHeight,false);
      invalidate();
      //requestLayout();
    }
    /**
     * Method changeModeTouch : change modeTouch if is 0 change to 1 else change to 0
     * */
    public void changeModeTouch(){
        if(modeTouch == 0){
            modeTouch =1;
        }else{
            modeTouch = 0;
        }
    }

    /**
     * Method getModeTouch
     * @return modeTouch
     * */
    public int getModeTouch(){
        return modeTouch;
    }
    /**
     * Class ScaleListener is custom setting of SimpleOnScaleGestureListener
     ** */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            /*mScaleFactor *= scaleGestureDetector.getScaleFactor();
            float m_MIN_ZOOM = 1.0f;
            float m_MAX_ZOOM = 3.0f;
            mScaleFactor = Math.max(m_MIN_ZOOM,Math.min(mScaleFactor, m_MAX_ZOOM));
            invalidate();*/
            float facto = scaleGestureDetector.getScaleFactor();
            matrix.postScale(facto,facto,getWidth()/2f,getHeight()/2f);
            ViewCompat.postInvalidateOnAnimation(ListFigure.this);
            return true;
        }
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector){
            return true;
        }
    }
    GestureDetector.OnGestureListener listener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2 , float dX, float dY){
            matrix.postTranslate(-dX,-dY);
            ViewCompat.postInvalidateOnAnimation(ListFigure.this);
         return  true;
        }

    };


    @Override
    protected  void onSizeChanged(int w, int h ,int oldw, int oldh){
        float scale = Math.max(w/mBoardWidth,h/mBoardHeght);
        matrix.setScale(scale,scale);
        matrix.postTranslate((w-scale*mBoardWidth)/2f,(h-scale*mBoardHeght)/2f);
    }
    /**
     * Method changeOrderList : change a element in the list of figures
     * if and only if the element is not the index entered as a parameter and getX and getY are inside
     * @param getX x of point
     * @param getY y of point
     * @param index
     * * */
    public void changeOrderList(float getX, float getY , int index){
        for(int i = 0; i<myFigures.size();i++){
            if(i!=index){
                if(myFigures.get(i) instanceof  Point) {
                    Point temp = (Point) myFigures.get(i);
                    float distance = distancePoint_to_Point(getX, getY, temp.getCenterX(), temp.getCenterY());
                    if(distance <= acceptDistance*2){
                        myFigures.remove(i);
                        myFigures.add(0,temp);
                    }
                }else if (myFigures.get(i) instanceof Circle) {
                    Circle temp = (Circle) myFigures.get(i);
                    float distance = distancePoint_to_Point(getX, getY, temp.getCenterX(), temp.getCenterY());
                    if (distance <= temp.getRadius()) {
                        myFigures.remove(i);
                        myFigures.add(0,temp);
                    }
                } else if (myFigures.get(i) instanceof Rectangle) {
                    Rectangle temp = (Rectangle) myFigures.get(i);
                    if (getX <= temp.getRight() && getX >= temp.getLeft() && getY <= temp.getBottom() && getY >= temp.getTop()) {
                        myFigures.remove(i);
                        myFigures.add(0,temp);
                    }
                } else if (myFigures.get(i) instanceof Line) {
                    Line temp = (Line) myFigures.get(i);
                    float distanceFirstPoint = distancePoint_to_Point(getX,getY,temp.getStartX(),temp.getStartY());
                    float distanceSecondPoint = distancePoint_to_Point(getX,getY,temp.getStopX(),temp.getStopY());
                    if (temp.distancePoint_to_Line(getX, getY) <= acceptDistance
                            &&distanceFirstPoint>acceptDistance && distanceSecondPoint>acceptDistance) {
                        myFigures.remove(i);
                        myFigures.add(0,temp);
                    }
                } else if (myFigures.get(i) instanceof Ellipse) {
                    Ellipse temp = (Ellipse) myFigures.get(i);
                    if(temp.pointEquationX1(getY) >= getX && temp.pointEquationX2(getY) <= getX
                            && temp.pointEquationY1(getX) >= getY && temp.pointEquationY2(getX) <= getY){
                        myFigures.remove(i);
                        myFigures.add(0,temp);
                    }
                }
            }
        }
    }

    /**
     * Method initialStyleFigure define the parameters initials of the pencil Paint
     */
    public void initialStyleFigure(){
        float[] intervals = new float[]{0.0f, 0.0f};
        float phase = 0;
        DashPathEffect dashPathEffect = new DashPathEffect(intervals, phase);
        pencil = new Paint();
        pencil.setAntiAlias(true);
        pencil.setARGB(250, figureColour[0],figureColour[1],figureColour[2]);
        pencil.setStrokeWidth(4);
        pencil.setStyle(Paint.Style.STROKE);
        pencil.setPathEffect(dashPathEffect);
    }//End Method
    /**
     * Method addPoint add a point in the list of figures
     * @param _centerX Define the position of the point
     * @param _centerY Define the position of the point*/
    public void addPoint(float _centerX, float _centerY) {
        Point aux = new Point(_centerX, _centerY, pencil,figureColour);
        this.myFigures.add(aux);
        figureSelected = myFigures.size()-1;
        invalidate();
    }//End Method
    /**
     * Method addCircle add a circle in the list of figures
     * @param _startX Define the position of the circle
     * @param _startY Define the position of the circle
     * @param _radius Define the radius od the circle*/
    public void addCircle(float _startX, float _startY, float _radius) {
        Circle aux = new Circle(_startX, _startY, _radius, pencil,figureColour);
        this.myFigures.add(aux);
        figureSelected = myFigures.size()-1;
        invalidate();
    }//End Method
    /**
     * Method addRectangle add a rectangle in the list of figures
     * @param _left Define the position of the rectangle from the left
     * @param _top Define the position of the rectangle from above
     * @param _right Define the position of the rectangle from the right
     * @param _bottom Define the position of the rectangle from the below*/
    public void addRectangle(float _left, float _top, float _right, float _bottom) {
        Rectangle aux = new Rectangle(_left, _top, _right, _bottom, pencil,figureColour);
        this.myFigures.add(aux);
        figureSelected = myFigures.size()-1;
        invalidate();
    }//End Method
    /**
     * Method addEllipse add a ellipse in the list of figures
     * @param _left Define the position of the ellipse from the left
     * @param _top Define the position of the ellipse from above
     * @param _right Define the position of the ellipse from the right
     * @param _bottom Define the position of the ellipse from the below*/
    public void addEllipse(float _left, float _top, float _right, float _bottom) {
        Ellipse aux = new Ellipse(_left, _top, _right, _bottom, pencil,figureColour);
        this.myFigures.add(aux);
        figureSelected = myFigures.size()-1;
        invalidate();
    }//End Method
    /**
     * Method addLine add a line in the list of figures
     * @param _startX Define the start Coordinate X
     * @param _startY Define the start Coordinate Y
     * @param _stopX Define the stop Coordinate X
     * @param _stopY Define the stop Coordinate Y*/
    public void addLine(float _startX , float _startY, float _stopX, float _stopY) {
        Line aux = new Line(_startX, _startY, _stopX, _stopY, pencil,figureColour);
        this.myFigures.add(aux);
        figureSelected = myFigures.size()-1;
        invalidate();
    }//End Method
    /**
     * Method deleteFigure the selected figure is deleted
     * */
    public boolean deleteFigure() {
        if (myFigures.size()>0 && this.figureSelected != -1 ) {
            myFigures.remove(this.figureSelected);
            figureSelected = -1;
            invalidate(); //Redraw
            return true;
        }else{
            return false;
        }
    }//End Method
    /**
     * Method changeColour the selected figure changes color
     * */
    public boolean changeColour(int [] colour) {
        if (myFigures.size() >0 && this.figureSelected != -1) {
            myFigures.get(this.figureSelected).setColour(colour);
            myFigures.get(this.figureSelected).getPaint().setARGB(255,colour[0],colour[1],colour[2]);
            invalidate(); //Redraw
            return true;
        }else {
            return false;
        }
    }//End Method
    /**
     * Method toString show list of figures content
     * @return content of the list*/
    public String toString(){
        StringBuilder listJson = new StringBuilder("[\n");
        for(int i=0;i<this.myFigures.size();i++) {
            listJson.append(myFigures.get(i).toString()).append("\n");
            if (i < this.myFigures.size() - 1)
                listJson.append(",");
        }
        return listJson+"]";
    }//End Method
    /*
     Method clearList deleted the list
     public void clearList(){
     this.myFigures.clear();
     }
    */
    /*Method toUpdate reDraw the content - is invalidate method
     public void toUpdate() {
     invalidate();
    }
    */

    /**
     * Method distancePoint to Point define tha distance between two points
     * @param pointX X Coordinate of the first point
     * @param pointX1 X Coordinate of the second point
     * @param pointY Y Coordinate of the first point
     * @param pointY1 Y Coordinate of the second point
     * @return distance*/
    public float distancePoint_to_Point(float pointX, float pointY, float pointX1, float pointY1){
        float valX = pointX - pointX1;
        float valY = pointY - pointY1;
        double distance = Math.sqrt(valX * valX + valY * valY);
        return (float) distance;
    }//Closing the Method

    /**
     * Method onDraw draw the figure geometric
     * @param canvas area of draw*/
    @SuppressLint("CanvasSize")
    protected void onDraw(Canvas canvas) {
        generalWidth = canvas.getWidth();
        generalHeight = canvas.getHeight();
        if(mImage !=null){
            canvas.save();
            //canvas.concat(matrix);
            //mBoard.draw(canvas);
            canvas.drawBitmap(mImage,0,0,null);
            canvas.restore();
        }
        for (int i = 0; i < myFigures.size(); i++) {
            if(myFigures.get(i) instanceof  Point) {
                Point temp = (Point) myFigures.get(i);
                canvas.drawPoint(temp.getCenterX(),temp.getCenterY(),Util.Circle(temp.getColour()));
                canvas.drawCircle(temp.getCenterX(),temp.getCenterY(),acceptDistance*2,Util.Circle(temp.getColour()));
                if(i == figureSelected){
                    canvas.drawCircle(temp.getCenterX(),temp.getCenterY(),acceptDistance,Util.CircleTransparent(temp.getColour()));
                }
            }else if (myFigures.get(i) instanceof Circle) {
                Circle temp = (Circle) myFigures.get(i);
                canvas.drawCircle(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), Util.Circle(temp.getColour()));
                canvas.drawCircle(temp.getCenterX()+temp.getRadius(), temp.getCenterY(), acceptDistance/3, Util.CircleSmall(temp.getColour()));
                if(i == figureSelected){
                    canvas.drawCircle(temp.getCenterX()+temp.getRadius(), temp.getCenterY(), acceptDistance, Util.CircleTransparent(temp.getColour()));
                }
            } else if (myFigures.get(i) instanceof Rectangle) {
                Rectangle temp = (Rectangle) myFigures.get(i);
                canvas.drawRect(temp.getLeft(), temp.getTop(), temp.getRight(), temp.getBottom(), Util.Circle(temp.getColour()));
                canvas.drawCircle(temp.getLeft(), temp.getTop(), acceptDistance/3,  Util.CircleSmall(temp.getColour()));
                canvas.drawCircle(temp.getRight(), temp.getBottom(), acceptDistance/3,  Util.CircleSmall(temp.getColour()));
                if(i == figureSelected){
                    canvas.drawCircle(temp.getLeft(), temp.getTop(), acceptDistance, Util.CircleTransparent(temp.getColour()));
                    canvas.drawCircle(temp.getRight(), temp.getBottom(), acceptDistance, Util.CircleTransparent(temp.getColour()));
                }
            }else if (myFigures.get(i) instanceof Line) {
                Line temp = (Line) myFigures.get(i);
                canvas.drawLine(temp.getStartX(), temp.getStartY(), temp.getStopX(), temp.getStopY(), Util.Circle(temp.getColour()));
                canvas.drawCircle(temp.getStartX(), temp.getStartY(), acceptDistance/3,  Util.CircleSmall(temp.getColour()));
                canvas.drawCircle(temp.getStopX(), temp.getStopY(), acceptDistance/3,  Util.CircleSmall(temp.getColour()));
                if(i == figureSelected){
                    canvas.drawCircle(temp.getStartX(), temp.getStartY(), acceptDistance, Util.CircleTransparent(temp.getColour()));
                    canvas.drawCircle(temp.getStopX(), temp.getStopY(), acceptDistance, Util.CircleTransparent(temp.getColour()));
                }
            } else if (myFigures.get(i) instanceof Ellipse) {
                Ellipse temp = (Ellipse) myFigures.get(i);
                @SuppressLint("DrawAllocation") RectF rectAux = new RectF(temp.getLeft(),temp.getTop(),temp.getRight(),temp.getBottom());
                canvas.drawOval(rectAux,Util.Circle(temp.getColour()));
                canvas.drawCircle((temp.getLeft()+temp.getRight())/2, temp.getTop(), acceptDistance/3, Util.CircleSmall(temp.getColour()));
                canvas.drawCircle(temp.getRight(), (temp.getBottom()+temp.getTop())/2, acceptDistance/3, Util.CircleSmall(temp.getColour()));
                canvas.drawCircle((temp.getLeft()+temp.getRight())/2, temp.getBottom(), acceptDistance/3, Util.CircleSmall(temp.getColour()));
                canvas.drawCircle(temp.getLeft(), (temp.getBottom()+temp.getTop())/2, acceptDistance/3, Util.CircleSmall(temp.getColour()));
                if(i == figureSelected){
                    canvas.drawCircle((temp.getLeft()+temp.getRight())/2, temp.getTop(),  acceptDistance, Util.CircleTransparent(temp.getColour()));
                    canvas.drawCircle(temp.getRight(), (temp.getBottom()+temp.getTop())/2, acceptDistance, Util.CircleTransparent(temp.getColour()));
                    canvas.drawCircle((temp.getLeft()+temp.getRight())/2, temp.getBottom(),  acceptDistance, Util.CircleTransparent(temp.getColour()));
                    canvas.drawCircle(temp.getLeft(), (temp.getBottom()+temp.getTop())/2, acceptDistance, Util.CircleTransparent(temp.getColour()));
                }
            } else {
                System.out.println("No Tips");
            }
        }
        //canvas.translate(mPositionX,mPositionY);
        //canvas.scale(mScaleFactor,mScaleFactor);



        //canvas.restore();
    }//End Method
    /**
     * Method check check the dimensions of the figure Point in its container
     * @param x of point
     * @param y of point */
    public boolean check(float x,float y){
        return x< generalWidth &&
                x>0 &&
                y<generalHeight &&
                y>0;
    }//End Method

    /**
     * Method checkPoint check the dimensions of the figure Point in its container
     * @param temp Point figure */
    public boolean checkPoint(Point temp){
        return temp.getCenterX()< generalWidth &&
                temp.getCenterX()>0 &&
                temp.getCenterY()<generalHeight &&
                temp.getCenterY()>0;
    }//End Method

    /**
     * Method adaptPoint change and adapt the dimensions of the figure Point in its container
     * @param temp Point figure */
    public void adaptPoint(Point temp){
        if(temp.getCenterX() >= generalWidth ){
                temp.setCenterX(generalWidth  - 1);
        }else if(temp.getCenterX()<=0){
            temp.setCenterX(1);
        }
        if(temp.getCenterY()>=generalHeight){
            temp.setCenterY(generalHeight - 1);
        }else if(temp.getCenterY()<=0){
            temp.setCenterY(1);
        }
    }//End Method

    /**
     * Method checkCircle check the dimensions of the figure Circle in its container
     * @param temp Circle figure */
    public boolean checkCircle(Circle temp){
        return temp.getCenterX()+temp.getRadius() < generalWidth &&
                temp.getCenterX()-temp.getRadius()>0 &&
                temp.getCenterY()+temp.getRadius()<generalHeight &&
                temp.getCenterY()-temp.getRadius()>0;
    }//End Method

    /**
     * Method adaptCircle change and adapt the dimensions of the figure Circle in its container
     * @param temp Circle figure */
    public void adaptCircle(Circle temp){
        if(temp.getCenterX()+temp.getRadius() >= generalWidth ){
            if(generalWidth-temp.getRadius()-1-temp.getRadius()<=0){
                temp.setRadius(temp.getRadius()-1);
            }else {
                temp.setCenterX(generalWidth - temp.getRadius() - 1);
            }
        }else if(temp.getCenterX()-temp.getRadius()<=0){
            temp.setCenterX(temp.getRadius()+1);
        }
        if(temp.getCenterY()+temp.getRadius()>=generalHeight){
            if(generalHeight-temp.getRadius()-1-temp.getRadius()<=0){
                temp.setRadius(temp.getRadius()-1);
            }else {
                temp.setCenterY(generalHeight - temp.getRadius() - 1);
            }
        }else if(temp.getCenterY()-temp.getRadius()<=0){
            temp.setCenterY(temp.getRadius()+1);
        }
    }//End Method

    /**
     * Method checkEllipse check the dimensions of the figure Ellipse in its container
     * @param temp Ellipse figure */
    public boolean checkEllipse(Ellipse temp){
        return temp.getLeft() >= 0 && temp.getRight() <= generalWidth && temp.getBottom() <= generalHeight && temp.getTop() >= 0;
    }//End Method

    /**
     * Method adaptEllipse change and adapt the dimensions of the figure Ellipse in its container
     * @param  temp Rectangle figure */
    public void adaptEllipse(Ellipse temp){
        //Dimensions of the Ellipse
        float widthRectangle = Math.abs(temp.getRight() - temp.getLeft());
        float heightRectangle = Math.abs(temp.getBottom() - temp.getTop());
        if(temp.getLeft()<0){
            temp.setLeft(1);
            if(widthRectangle+1>generalWidth){
                temp.setRight(generalWidth-2);
            }else {
                temp.setRight(widthRectangle + 1);
            }
        }else if(temp.getRight()>generalWidth){
            temp.setRight(generalWidth-1);
            temp.setLeft(generalWidth-widthRectangle-1);
        }else if(temp.getTop()<0){
            temp.setTop(1);
            if(heightRectangle+1>generalHeight){
                temp.setBottom(generalHeight-2);
            }else{
                temp.setBottom(heightRectangle+1);
            }
        }else if(temp.getBottom()>generalHeight){
            temp.setTop(generalHeight-heightRectangle-1);
            temp.setBottom(generalHeight-1);
        }

    }//End Method

    /**
     * Method checkRectangle check the dimensions of the figure Rectangle in its container
     * @param aux Rectangle figure */
    public boolean checkRectangle(Rectangle aux){
        return aux.getLeft()>=0 && aux.getRight()<=generalWidth && aux.getBottom()<=generalHeight&& aux.getTop()>=0;
    }//End Method

    /**
     * Method adaptRectangle change and adapt the dimensions of the figure Rectangle in its container
     * @param  aux Rectangle figure */
    public void adaptRectangle(Rectangle aux){
        //Dimensions of the Rectangle
        float widthRectangle = (aux.getRight() - aux.getLeft());
        float heightRectangle = (aux.getBottom() - aux.getTop());
        if(aux.getLeft()<0){
            aux.setLeft(1);
            if(widthRectangle+1>generalWidth){
                aux.setRight(generalWidth-2);
            }else {
                aux.setRight(widthRectangle + 1);
            }
        }else if(aux.getRight()>generalWidth){
            aux.setRight(generalWidth-1);
            aux.setLeft(generalWidth-widthRectangle-1);
        }else if(aux.getTop()<0){
            aux.setTop(1);
            if(heightRectangle+1>generalHeight){
                aux.setBottom(generalHeight-2);
            }else{
                aux.setBottom(heightRectangle+1);
            }
        }else if(aux.getBottom()>generalHeight){
            aux.setTop(generalHeight-heightRectangle-1);
            aux.setBottom(generalHeight-1);
        }
    }//End Method

    /**
     * Method checkLine check the dimensions of the figure line in its container
     * @param temp Line figure */
    public boolean checkLine(Line temp){
        return temp.getStartX()>=0 && temp.getStopX()<=generalWidth &&
                temp.getStartY()>=0 && temp.getStopY()<=generalHeight
                && temp.getStopX()>=0 && temp.getStartX()<=generalWidth
                &&  temp.getStopY()>=0 && temp.getStartY()<=generalHeight;
    }//End Method

    /**
     * Method adaptLine change and adapt the dimensions of the figure line in its container
     * @param  temp Line figure */
    public void adaptLine(Line temp){
        //Dimensions of the Line
        float prolongWidth = Math.abs(temp.getStopX()-temp.getStartX());
        float prolongHeight = Math.abs(temp.getStopY()-temp.getStartY());
        if(temp.getStartX()<0) {
            temp.setStartX(1);
            if (prolongWidth + 1 > generalWidth) {
                temp.setStopX(generalWidth - 2);
            } else {
                temp.setStopX(prolongWidth + 1);
            }
        }else if(temp.getStopX()<0) {
            temp.setStopX(1);
            if (prolongWidth + 1 > generalWidth) {
                temp.setStartX(generalWidth - 2);
            } else {
                temp.setStartX(prolongWidth + 1);
            }
        }else if( temp.getStopX()>generalWidth) {
            temp.setStopX(generalWidth - 1);
            temp.setStartX(generalWidth - prolongWidth - 1);
        }else if( temp.getStartX()>generalWidth) {
            temp.setStartX(generalWidth - 1);
            temp.setStopX(generalWidth - prolongWidth - 1);
        }else if(temp.getStartY()<0) {
            temp.setStartY(1);
            if (prolongHeight + 1 > generalHeight) {
                temp.setStopY(generalHeight - 2);
            } else {
                temp.setStopY(prolongHeight + 1);
            }
        }else if(temp.getStopY()<0) {
            temp.setStopY(1);
            if (prolongHeight + 1 > generalHeight) {
                temp.setStartY(generalHeight - 2);
            } else {
                temp.setStartY(prolongHeight + 1);
            }
        }else if(temp.getStopY()>generalHeight) {
            temp.setStartY(generalHeight - prolongHeight - 1);
            temp.setStopY(generalHeight - 1);
        }else if(temp.getStartY()>generalHeight) {
            temp.setStopY(generalHeight - prolongHeight - 1);
            temp.setStartY(generalHeight - 1);
        }
    }//End Method
    public void checkFigures(){
        for (int i = 0; i < myFigures.size(); i++) {
            if (myFigures.get(i) instanceof Circle){
                Circle temp = (Circle) myFigures.get(i);
                if(!checkCircle(temp))
                    adaptCircle(temp);
            }else if (myFigures.get(i) instanceof Rectangle){
                Rectangle temp = (Rectangle) myFigures.get(i);
                if(!checkRectangle(temp))
                    adaptRectangle(temp);
            }else if (myFigures.get(i) instanceof Point){
                Point temp = (Point) myFigures.get(i);
                if(!checkPoint(temp))
                    adaptPoint(temp);
            }else if (myFigures.get(i) instanceof Line){
                Line temp = (Line) myFigures.get(i);
                if(!checkLine(temp))
                    adaptLine(temp);
            }else if (myFigures.get(i) instanceof Ellipse){
                Ellipse temp = (Ellipse) myFigures.get(i);
                if(!checkEllipse(temp))
                    adaptEllipse(temp);
            }
        }
    }

    public float getXTouch(){
        return this.touchX;
    }
    public  float getYTouch(){
        return this.touchY;
    }
    public float dX(){
        return d;
    }
    public float dY(){
        return d_1;
    }

    private int mActivePointerId = INVALID_POINTER_ID;
    /**
     * Method onTouchEvent
     * @param event Events of touch*/
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();
        float getX = event.getX();
        float getY = event.getY();
        //int acct = event.getAction();
        //scaleGestureDetector.onTouchEvent(event);
        //mGestureDetector.onTouchEvent(event);
        final int acct =   event.getActionMasked();
        if (layout.getTranslationX() >= 0) { //-
            d = (layout.getScaleX() * layout.getWidth() - layout.getWidth()) - layout.getTranslationX() ;
        }else { //+
            d = (layout.getScaleX() * layout.getWidth() - layout.getWidth()) + layout.getTranslationX(); //
        }
        if (layout.getTranslationY() >= 0) {//-
            d_1 = (layout.getScaleY() * layout.getHeight() - layout.getHeight()) - layout.getTranslationY();
        }
        else {//+
            d_1 = (layout.getScaleY() * layout.getHeight() - layout.getHeight()) + layout.getTranslationY();//
        }
       //Mode Zoom
        if(modeTouch == 1) {
            scaleGestureDetector.onTouchEvent(event);
            invalidate();
            switch (acct) {
                case MotionEvent.ACTION_DOWN: {
                    final int pointerIndex = event.getActionIndex();
                    getX = event.getX(pointerIndex);
                    getY = event.getY(pointerIndex);
                    getPastX = getX;
                    getPastY = getY;
                    mActivePointerId = event.getPointerId(0);
                    break;

                }
                case MotionEvent.ACTION_MOVE: {
                    final int pointerIndex = event.findPointerIndex(mActivePointerId);
                    getX = event.getX(pointerIndex);
                    getY = event.getY(pointerIndex);
                    if (!scaleGestureDetector.isInProgress() && layout.getScaleX() > 1f && layout.getScaleY() > 1f) {
                        if ((getX - (getPastX - layout.getTranslationX())) < d && (getX - (getPastX - layout.getTranslationX())) > -d)
                            layout.setTranslationX(getX - (getPastX - layout.getTranslationX()));
                        if ((getY - (getPastY - layout.getTranslationY())) < d_1 && (getY - (getPastY - layout.getTranslationY())) > -d_1)
                            layout.setTranslationY(getY - (getPastY - layout.getTranslationY()));
                    }
                    getPastX = getX;
                    getPastY = getY;
                    break;

                }
                case MotionEvent.ACTION_UP: {
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }
                case MotionEvent.ACTION_CANCEL: {
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }
                case MotionEvent.ACTION_POINTER_UP: {
                    final int pointerIndex = event.getActionIndex();
                    final int pointerId = event.getPointerId(pointerIndex);
//                getPastX = getX;
                    //              getPastY = getY;
                    if (layout.getScaleX() <= 1f && layout.getScaleY() <= 1f) {
                        layout.animate().translationX(0).translationY(0);
                    } else {
                        if (layout.getTranslationX() > d)
                            layout.animate().translationX(d);
                        if (layout.getTranslationY() > d_1)
                            layout.animate().translationY(d_1);
                        if (layout.getTranslationX() < -d)
                            layout.animate().translationX(-d);
                        if (layout.getTranslationY() < -d_1)
                            layout.animate().translationY(-d_1);
                    }
                    if (pointerId == mActivePointerId) {
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        getPastX = event.getX(newPointerIndex);
                        getPastY = event.getY(newPointerIndex);
                        mActivePointerId = event.getPointerId(newPointerIndex);
                    }
                    break;
                }
            }
            return true;
        }

        //Mode Figures
        switch (acct) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = event.getActionIndex();
                getX = event.getX(pointerIndex);
                getY = event.getY(pointerIndex);
                this.figureSelected = -1;
                invalidate();
                getPastX = getX;
                getPastY = getY;
                mActivePointerId = event.getPointerId(0);
                for (int i = 0; i < myFigures.size(); i++) {
                    if (myFigures.get(i) instanceof Point) {
                        Point temp = (Point) myFigures.get(i);
                        //distance with the origin(centerX,centerY)
                        float distance = distancePoint_to_Point(getX, getY, temp.getCenterX(), temp.getCenterY());
                        if (distance <= acceptDistance * 2) { //Move
                            mode = 2;
                            this.figureSelected = i;
                            changeOrderList(getX, getY, i);
                        }
                    } else if (myFigures.get(i) instanceof Circle) {
                        //float acceptDistanceCircle = acceptDistance;
                        Circle temp = (Circle) myFigures.get(i);
                        //distance with the origin
                        float distance = distancePoint_to_Point(getX, getY, temp.getCenterX(), temp.getCenterY());
                        //distance with the point pivot
                        float distance_ = distancePoint_to_Point(getX, getY, temp.getCenterX() + temp.getRadius(), temp.getCenterY());
                        if (distance <= temp.getRadius()) { //Move
                            mode = 2;
                            this.figureSelected = i;
                            changeOrderList(getX, getY, i);
                        }
                        if (distance_ <= acceptDistance) { //Resize
                            mode = 1;
                            this.figureSelected = i;
                        }
                    } else if (myFigures.get(i) instanceof Rectangle) {
                        Rectangle temp = (Rectangle) myFigures.get(i);
                        //distance a the first point of interaction
                        float distanceFirstPoint = distancePoint_to_Point(getX, getY, temp.getLeft(), temp.getTop());
                        //distance a the second point of interaction
                        float distanceSecondPoint = distancePoint_to_Point(getX, getY, temp.getRight(), temp.getBottom());
                        if (getX <= temp.getRight() && getX >= temp.getLeft() && getY <= temp.getBottom() && getY >= temp.getTop()) {
                            //Move
                            mode = 2;
                            this.figureSelected = i;
                            changeOrderList(getX, getY, i);
                        }
                        if (distanceFirstPoint <= acceptDistance || distanceSecondPoint <= acceptDistance) {
                            //Resize
                            mode = 1;
                            this.figureSelected = i;
                        }
                    } else if (myFigures.get(i) instanceof Line) {
                        Line temp = (Line) myFigures.get(i);
                        //distance a the first point of interaction
                        float distanceFirstPoint = distancePoint_to_Point(getX, getY, temp.getStartX(), temp.getStartY());
                        //distance a the second point of interaction
                        float distanceSecondPoint = distancePoint_to_Point(getX, getY, temp.getStopX(), temp.getStopY());
                        if (temp.distancePoint_to_Line(getX, getY) <= acceptDistance) {
                            //Move
                            mode = 2;
                            this.figureSelected = i;
                            changeOrderList(getX, getY, i);
                        }
                        if (distanceFirstPoint <= acceptDistance || distanceSecondPoint <= acceptDistance) {
                            //Resize
                            mode = 1;
                            this.figureSelected = i;
                        }
                    } else if (myFigures.get(i) instanceof Ellipse) {
                        Ellipse temp = (Ellipse) myFigures.get(i);
                        //distance a the first point of interaction
                        float distanceFirstPoint = distancePoint_to_Point(getX, getY, temp.getLeft(), temp.getBottom() / 2 + temp.getTop() / 2);
                        //distance a the second point of interaction
                        float distanceSecondPoint = distancePoint_to_Point(getX, getY, temp.getRight(), temp.getBottom() / 2 + temp.getTop() / 2);
                        //distance a the thirst point of interaction
                        float distanceThirstPoint = distancePoint_to_Point(getX, getY, temp.getRight() / 2 + temp.getLeft() / 2, temp.getTop());
                        //distance a the forty point of interaction
                        float distanceFortyPoint = distancePoint_to_Point(getX, getY, temp.getRight() / 2 + temp.getLeft() / 2, temp.getBottom());
                        if (temp.pointEquationX1(getY) >= getX && temp.pointEquationX2(getY) <= getX && temp.pointEquationY1(getX) >= getY && temp.pointEquationY2(getX) <= getY) {
                            //Move
                            mode = 2;
                            this.figureSelected = i;
                            changeOrderList(getX, getY, i);
                        }
                        if (distanceFirstPoint <= acceptDistance || distanceSecondPoint <= acceptDistance || distanceThirstPoint <= acceptDistance || distanceFortyPoint <= acceptDistance) {
                            //Resize
                            mode = 1;
                            this.figureSelected = i;
                        }
                    } else {
                        //Type of Mode (is not selection)
                        mode = 0;
                        invalidate();
                    }
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = event.findPointerIndex(mActivePointerId);
                getX = event.getX(pointerIndex);
                getY = event.getY(pointerIndex);

                if (this.figureSelected > -1 && !scaleGestureDetector.isInProgress()) {
                    if (myFigures.get(figureSelected) instanceof Point) {
                        Point temp = (Point) myFigures.get(figureSelected);
                        //checkPoint check dimensions of the point
                        if (checkPoint(temp)) {
                            if (mode == 1) { //ReSize
                                System.out.println("Error");
                            }
                            if (mode == 2) { //Move
                                temp.setCenterX(temp.getCenterX() - (getPastX - getX));
                                temp.setCenterY(temp.getCenterY() - (getPastY - getY));
                                invalidate();
                            }
                        } else {
                            adaptPoint(temp);
                        }
                    } else if (myFigures.get(figureSelected) instanceof Circle) {
                        Circle temp = (Circle) myFigures.get(figureSelected);
                        //checkCircle check dimensions of the circle
                        if (checkCircle(temp)) {
                            if (mode == 1) { //ReSize
                                // 80 is the radius acceptable
                                if ((temp.getRadius() - (getPastX - getX)) >= 80 && check(getX, getY)) {
                                    temp.setRadius(temp.getRadius() + (getX - getPastX));
                                    invalidate();
                                }
                            }
                            if (mode == 2) { //Move
                                //changeOrderList(getX,getY,figureSelected);
                                temp.setCenterX(temp.getCenterX() - (getPastX - getX));
                                temp.setCenterY(temp.getCenterY() - (getPastY - getY));
                                invalidate();
                            }
                        } else {
                            adaptCircle(temp);
                        }
                    } else if (myFigures.get(figureSelected) instanceof Rectangle) {
                        Rectangle aux = (Rectangle) myFigures.get(figureSelected);
                        //distance a the first point of interaction
                        float distanceFirstPoint = distancePoint_to_Point(getX, getY, aux.getLeft(), aux.getTop());
                        //distance a the second point of interaction
                        float distanceSecondPoint = distancePoint_to_Point(getX, getY, aux.getRight(), aux.getBottom());
                        //checkRectangle check dimensions of the Rectangle
                        if (checkRectangle(aux)) {
                            if (mode == 1 && check(getX, getY)) { //Resize
                                if (distanceFirstPoint <= acceptDistance) {
                                    //100x100 is Dimension of Rectangle limit
                                    if (aux.getRight() - aux.getLeft() > 100 && aux.getBottom() - aux.getTop() > 100) {
                                        aux.setLeft(getX);
                                        aux.setTop(getY);
                                        invalidate();
                                    } else {
                                        //100+1 for no limit dimension
                                        if (aux.getRight() - aux.getLeft() <= 100) {
                                            aux.setLeft(aux.getRight() - 101);
                                            invalidate();
                                        } else if (aux.getBottom() - aux.getTop() <= 100) {
                                            aux.setTop(aux.getBottom() - 101);
                                            invalidate();
                                        }
                                    }
                                }
                                if (distanceSecondPoint <= acceptDistance) {
                                    //100x100 is Dimension of Rectangle limit
                                    if (aux.getRight() - aux.getLeft() > 100 && aux.getBottom() - aux.getTop() > 100) {
                                        aux.setRight(getX);
                                        aux.setBottom(getY);
                                        invalidate();
                                    } else {
                                        //100+1 for no limit dimension
                                        if (aux.getRight() - aux.getLeft() <= 100) {
                                            aux.setRight(aux.getLeft() + 101);
                                            invalidate();
                                        } else if (aux.getBottom() - aux.getTop() <= 100) {
                                            aux.setBottom(aux.getTop() + 101);
                                            invalidate();
                                        }
                                    }
                                }
                            }
                            if (mode == 2) { //Move
                                aux.setRight(getX + aux.getRight() - getPastX);
                                aux.setBottom(getY + aux.getBottom() - getPastY);
                                aux.setLeft(getX - (getPastX - aux.getLeft()));
                                aux.setTop(getY - (getPastY - aux.getTop()));
                                invalidate();
                            }
                        } else {
                            //adaptRectangle Setter dimensions
                            adaptRectangle(aux);
                            invalidate();
                        }
                    } else if (myFigures.get(figureSelected) instanceof Line) {
                        Line temp = (Line) myFigures.get(figureSelected);
                        //distance a the first point of interaction
                        float distanceFirstPoint = distancePoint_to_Point(getX, getY, temp.getStartX(), temp.getStartY());
                        //distance a the second point of interaction
                        float distanceSecondPoint = distancePoint_to_Point(getX, getY, temp.getStopX(), temp.getStopY());
                        //distance a the line
                        //float distanceToLine = temp.distancePoint_to_Line(getX,getY);
                        if (checkLine(temp)) {
                            if (mode == 1 && check(getX, getY)) { //ReSize
                                if (distanceFirstPoint <= acceptDistance) {
                                    //50 is distance between points start and stop
                                    if (distancePoint_to_Point(temp.getStartX(), temp.getStartY(), temp.getStopX(), temp.getStopY()) <= 50) {
                                        if (temp.getStartX() < temp.getStopX() || temp.getStartY() < temp.getStopY()) {
                                            temp.setStartX(temp.getStartX() - 1);
                                            temp.setStartY(temp.getStartY() - 1);
                                            temp.setStopX(temp.getStopX() + 1);
                                            temp.setStopY(temp.getStopY() + 1);
                                            invalidate();
                                        } else {
                                            temp.setStartX(temp.getStartX() + 1);
                                            temp.setStartY(temp.getStartY() + 1);
                                            temp.setStopX(temp.getStopX() - 1);
                                            temp.setStopY(temp.getStopY() - 1);
                                            invalidate();
                                        }
                                    } else {
                                        temp.setStartX(getX);
                                        temp.setStartY(getY);
                                        invalidate();
                                    }
                                }
                                if (distanceSecondPoint <= acceptDistance) {
                                    temp.setStopX(getX);
                                    temp.setStopY(getY);
                                    invalidate();
                                }
                            }
                            if (mode == 2) { //Move
                                temp.setStopX(getX + temp.getStopX() - getPastX);
                                temp.setStopY(getY + temp.getStopY() - getPastY);
                                temp.setStartX(getX - (getPastX - temp.getStartX()));
                                temp.setStartY(getY - (getPastY - temp.getStartY()));
                                invalidate();
                            }
                        } else {
                            adaptLine(temp);
                        }
                    } else if (myFigures.get(figureSelected) instanceof Ellipse) {
                        Ellipse temp = (Ellipse) myFigures.get(figureSelected);
                        //distance a the first point of interaction
                        temp.updateParameters(temp);
                        float distanceFirstPoint = distancePoint_to_Point(getX, getY, temp.getLeft(), temp.getBottom() / 2 + temp.getTop() / 2);
                        //distance a the second point of interaction
                        float distanceSecondPoint = distancePoint_to_Point(getX, getY, temp.getRight(), temp.getBottom() / 2 + temp.getTop() / 2);
                        //distance a the third point of interaction
                        float distanceThirdPoint = distancePoint_to_Point(getX, getY, temp.getRight() / 2 + temp.getLeft() / 2, temp.getTop());
                        //distance a the forty point of interaction
                        float distanceFortyPoint = distancePoint_to_Point(getX, getY, temp.getRight() / 2 + temp.getLeft() / 2, temp.getBottom());
                        //Dimensions of the Ellipse
                        float widthEllipse = Math.abs(temp.getRight() - temp.getLeft());
                        float heightEllipse = Math.abs(temp.getBottom() - temp.getTop());
                        if (checkEllipse(temp)) {
                            if (mode == 1 && check(getX, getY)) { //Resize
                                if (distanceFirstPoint <= acceptDistance) {
                                    if (widthEllipse > 100) {
                                        temp.setLeft(getX);
                                        invalidate();
                                    } else {
                                        temp.setLeft(temp.getRight() - 101);
                                        invalidate();
                                    }
                                    temp.updateParameters(temp);
                                } else if (distanceSecondPoint <= acceptDistance) {
                                    if (widthEllipse > 100) {
                                        temp.setRight(getX);
                                        invalidate();
                                    } else {
                                        temp.setRight(temp.getLeft() + 101);
                                        invalidate();
                                    }
                                    temp.updateParameters(temp);
                                } else if (distanceThirdPoint <= acceptDistance) {
                                    if (heightEllipse > 100) {
                                        temp.setTop(getY);
                                        invalidate();
                                    } else {
                                        temp.setTop(temp.getBottom() - 101);
                                        invalidate();
                                    }
                                    temp.updateParameters(temp);
                                } else if (distanceFortyPoint <= acceptDistance) {
                                    if (heightEllipse > 100) {
                                        temp.setBottom(getY);
                                        invalidate();
                                    } else {
                                        temp.setBottom(temp.getTop() + 101);
                                        invalidate();
                                    }
                                    temp.updateParameters(temp);
                                }
                            }
                            if (mode == 2) { //Move
                                temp.setRight(getX + temp.getRight() - getPastX);
                                temp.updateParameters(temp);
                                temp.setBottom(getY + temp.getBottom() - getPastY);
                                temp.updateParameters(temp);
                                temp.setLeft(getX - (getPastX - temp.getLeft()));
                                temp.updateParameters(temp);
                                temp.setTop(getY - (getPastY - temp.getTop()));
                                temp.updateParameters(temp);
                                invalidate();
                            }
                        } else {
                            adaptEllipse(temp);
                        }
                    } else {
                        System.out.println("No Tips");
                    }
                } else {
                    /*if(!scaleGestureDetector.isInProgress()) {
                        final float distanceX = getX - getPastX;
                        final float distanceY = getY - getPastY;
                        mPositionX += distanceX;
                        mPositionY += distanceY;
                        invalidate(); //reDraw

                    }*/
                    /*if (layout.getScaleX() > 1f && layout.getScaleY() > 1f) {
                        if ((getX - (getPastX - layout.getTranslationX())) < d && (getX - (getPastX - layout.getTranslationX())) > -d)
                            layout.setTranslationX(getX - (getPastX - layout.getTranslationX()));
                        if ((getY - (getPastY - layout.getTranslationY())) < d_1 && (getY - (getPastY - layout.getTranslationY())) > -d_1)
                            layout.setTranslationY(getY - (getPastY - layout.getTranslationY()));
                    }*/
                }
                getPastX = getX;
                getPastY = getY;
                break;
            }
            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = event.getActionIndex();
                final int pointerId = event.getPointerId(pointerIndex);
                System.out.println("up:" + getX + ":" + getY);
                System.out.println("up:" + getPastX + ":" + getPastY);
                if (layout.getScaleX() <= 1f && layout.getScaleY() <= 1f) {
                    layout.animate().translationX(0).translationY(0);
                } else {
                    if (layout.getTranslationX() > d)
                        layout.animate().translationX(d);
                    if (layout.getTranslationY() > d_1)
                        layout.animate().translationY(d_1);
                    if (layout.getTranslationX() < -d)
                        layout.animate().translationX(-d);
                    if (layout.getTranslationY() < -d_1)
                        layout.animate().translationY(-d_1);
                }
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    getPastX = event.getX(newPointerIndex);
                    getPastY = event.getY( newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            }
        }
        checkFigures();
        return true;
    }//End Method
}
