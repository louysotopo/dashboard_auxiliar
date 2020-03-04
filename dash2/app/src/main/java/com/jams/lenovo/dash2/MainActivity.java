package com.jams.lenovo.dash2;
//Imports
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.sax.StartElementListener;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Toast;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Point;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import ListFigures.ListFigure;
import ListFigures.ListSegmentation;
import ListFigures.Util;

import static org.opencv.android.Utils.loadResource;



/**
 * This class define the Main Activity Image Ray-X editing space
 * @author Edwin Saavedra
 * @version 3
 */
public class MainActivity extends AppCompatActivity {
    protected static final int PICK_IMAGE =0;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    protected Uri imageurl;
    public  String currentPhotoPath;

    public static int TIME_ANIMATION = 100;
    public static float SCALE_ANIMATION = 1.1f;
    //Class Attributes--
    /*/Load and Save data
    private String nameFile;
    private String nameBinder;
    private File file;*/
    //Insert Figures
    private ImageView creatorCircles;
    private ImageView creatorRectangles;
    private ImageView creatorLines;
    private ImageView creatorEllipses;
    private ImageView creatorPoints;
    //Edit and delete Figures
    private ImageView deleteFigures;
    private ImageView changeColor;
    private int[] colour = {183, 149, 11};
    //Zoom Image
    private ImageView extendsImage;
    //Filters
    private ImageView filterImage;
    private Drawable d;
    //Change Layout
    private ImageView segmentation;
    //Layout for Image RX
    private LinearLayout layoutImageRx;
    private LinearLayout layoutImageRx1;
    //View
    private ListFigure myListFigures;
    private ListSegmentation myListSegmentation;
    //Animation
    private MyAnimation Animation;
    //Flags
    private boolean flagAnimation;
    private boolean flagAnimationColors;
    private boolean flagSegmentation;
    private boolean flagEraserSegmentation;
    //Scroll
    private LinearLayout menu_left;
    private LinearLayout menu_right;
    private LinearLayout getFigures;
    private LinearLayout backFilters;
    private ScrollView scrollLeft1;
    private ScrollView scrollLeft2;
    private ScrollView scrollLeft3;
    private ScrollView scrollRight1;
    private ScrollView scrollRight2;
    //Back and Checks
    private ImageView backMenuRight; //back menu right
    //Segments
    private ImageView deleteSegments;
    private ImageView changeColorSegments;
    private LinearLayout zoomImageLayout;
    private ImageView test1;
    private ImageView clearSegments;
    private ImageView eraserSegments;
    private ImageView pruebas;
    private ImageView import_;
    //Colors
    private ArrayList<ImageView> listColors;
    private int colorIndex;
    private ImageView color1;
    private ImageView color2;
    private ImageView color3;
    private ImageView color4;
    private ImageView color5;
    private ImageView color6;
    private ImageView color7;
    private ImageView color8;
    private ImageView color9;
    private ImageView color10;
    private ImageView color11;
    private ImageView color12;
    private ImageView color13;
    private ImageView color14;
    private ImageView color15;
    private ImageView color16;
    private ImageView color17;
    private ImageView color18;
    private ImageView color19;
    private ImageView color20;
    private ImageView color21;
    private ImageView color22;
    private ImageView color23;
    private ImageView color24;
    private ImageView color25;
    private ImageView iconColors;
    //Filters
    private MyFilters myFilters;
    private ImageView openCv;
    private ImageView openCv1;
    private ImageView openCv2;
    private ImageView openCv3;
    private ImageView openCv4;
    private ImageView openCv5;
    private ImageView openCv6;
    private ImageView openCv7;
    private ImageView openCv8;
    private ImageView openCv9;
    private ImageView openCv10;
    private ImageView openCv11;
    private ImageView openCv12;
    private ImageView openCv13;
    private ImageView openCv14;
    private ImageView openCv15;
    private ImageView openCv16;
    private ImageView openCv17;
    private ImageView openCv18;
    private ImageView openCv19;
    private ImageView openCv20;
    private ImageView openCv21;
    private ImageView openCv22;
    private ImageView openCv23;
    private ImageView openCv24;
    private ImageView openCv25;
    private ImageView openCv26;
    private ImageView openCv27;
    private ImageView openCv28;
    private SeekBar zoomBar;
    //img original format Bitmap
    private Bitmap original;
    private Mat img;
    private int nameImage;
    //--End Attributes of class
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ORIENTATION FALSE
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Add Layout Activity XML
        //OpenCVLoader.initDebug();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //Load OpenCV
        OpenCVLoader.initDebug();
        //Initializing Properties
        zoomBar = findViewById(R.id.zoomBar);
        zoomBar.getThumb().setColorFilter(Color.rgb(255, 127, 80), PorterDuff.Mode.MULTIPLY);

        initialProperties();

        zoomBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float scale = 1.0f + progress * 0.01f;

                if (layoutImageRx.getTranslationX() >= myListFigures.dX() && layoutImageRx.getTranslationY() >= myListFigures.dY()
                        && layoutImageRx.getTranslationX() <= -myListFigures.dX() && layoutImageRx.getTranslationY() <= -myListFigures.dY()) {
                    layoutImageRx.setScaleX(scale);
                    layoutImageRx.setScaleY(scale);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //layoutImageRx.setPivotX(myListFigures.getXTouch());
                //layoutImageRx.setPivotY(myListFigures.getYTouch());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println("data 1 : "+myListFigures.dX()+";"+myListFigures.dY());
                System.out.println("data 2 : "+layoutImageRx.getPivotX()+";"+layoutImageRx.getPivotY());
                System.out.println("data 3 : "+layoutImageRx.getTranslationX()+";"+layoutImageRx.getTranslationY());
                if(layoutImageRx.getTranslationX()>myListFigures.dX())
                    layoutImageRx.animate().translationX(myListFigures.dX());
                if(layoutImageRx.getTranslationY()>myListFigures.dY())
                    layoutImageRx.animate().translationY(myListFigures.dY());
                if(layoutImageRx.getTranslationX()<-myListFigures.dX())
                    layoutImageRx.animate().translationX(-myListFigures.dX());
                if(layoutImageRx.getTranslationY()<-myListFigures.dY())
                    layoutImageRx.animate().translationY(-myListFigures.dY());
         //       layoutImageRx.setPivotX(360);
           //     layoutImageRx.setPivotY(656);
            }
        });
        //Change of layout to "Segmentation"
        segmentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flagSegmentation) {
                    //myListSegmentation.addCircleSegmentation(12,12,12);
                    //myListSegmentation.clearList();
                    if(myListSegmentation.getModeTouch()==1)
                        extendsImage.setColorFilter(Color.rgb(255, 127, 80)); //orange
                    else
                        extendsImage.setColorFilter(Color.rgb(255, 255, 255)); //white
                    Animation.animationScale(segmentation, TIME_ANIMATION, SCALE_ANIMATION, SCALE_ANIMATION);
                    segmentation.setColorFilter(Color.rgb(255, 127, 80));
                    layoutImageRx.setVisibility(View.GONE);
                    layoutImageRx1.setVisibility(View.VISIBLE);
                    scrollLeft1.setVisibility(View.GONE);
                    if(flagAnimationColors) //menu de figuritas esta activo
                        scrollLeft2.setVisibility(View.VISIBLE);
                    else
                        scrollLeft2.setVisibility(View.GONE);
                    flagSegmentation = true;
                }else{
                    if(myListFigures.getModeTouch()==1)
                        extendsImage.setColorFilter(Color.rgb(255, 127, 80)); //orange
                    else
                        extendsImage.setColorFilter(Color.rgb(255, 255, 255)); //white

                    segmentation.setColorFilter(Color.rgb(255,255,255));
                    scrollLeft2.setVisibility(View.GONE);
                    if(flagAnimationColors)
                        scrollLeft1.setVisibility(View.VISIBLE);
                    else
                        scrollLeft1.setVisibility(View.GONE);
                    layoutImageRx1.setVisibility(View.GONE);
                    layoutImageRx.setVisibility(View.VISIBLE);
                    flagSegmentation = false;
                }
            }
        });
        clearSegments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation.animationScale(clearSegments,TIME_ANIMATION,SCALE_ANIMATION,SCALE_ANIMATION);
                myListSegmentation.clearList();
            }
        });
        eraserSegments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flagEraserSegmentation) {
                    Animation.animationScale(eraserSegments, TIME_ANIMATION, SCALE_ANIMATION, SCALE_ANIMATION);
                    eraserSegments.setColorFilter(Color.rgb(255, 127, 80));
                    myListSegmentation.changeModeTouch(2);
                    flagEraserSegmentation = true;
                }else{
                    eraserSegments.setColorFilter(Color.rgb(255, 255, 255));
                    myListSegmentation.changeModeTouch(0);
                    flagEraserSegmentation = false;
                }
            }
        });
        //back menu right
        backFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation.animationScale(backMenuRight,TIME_ANIMATION,SCALE_ANIMATION,SCALE_ANIMATION);
                filterImage.setColorFilter(Color.rgb(255,255,255));
                scrollRight2.setVisibility(View.GONE);
                backFilters.setVisibility(View.GONE);
                scrollRight1.setVisibility(View.VISIBLE);
                test1.setVisibility(View.VISIBLE);
            }
        });
        //Add Filter Image
        filterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test1.setVisibility(View.GONE);
                Animation.animationScale(filterImage,TIME_ANIMATION,SCALE_ANIMATION,SCALE_ANIMATION);
                filterImage.setColorFilter(Color.rgb(255,127,80));
                scrollRight1.setVisibility(View.GONE);
                scrollRight2.setVisibility(View.VISIBLE);
                backFilters.setVisibility(View.VISIBLE);
            }
        });
        //Zoom Image RX
        extendsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation.animationScale(extendsImage,TIME_ANIMATION,SCALE_ANIMATION,SCALE_ANIMATION);
                if(!flagSegmentation) {
                    if(myListFigures.getModeTouch()==0 ) {
                        extendsImage.setColorFilter(Color.rgb(255, 127, 80)); //orange
                    }else{
                        extendsImage.setColorFilter(Color.rgb(255, 255, 255)); //white
                    }
                    myListFigures.changeModeTouch();
                }else {

                    if(myListSegmentation.getModeTouch()==0 || myListSegmentation.getModeTouch()==2) {
                        extendsImage.setColorFilter(Color.rgb(255, 127, 80)); //orange
                        myListSegmentation.changeModeTouch(1);
                    }else{
                        extendsImage.setColorFilter(Color.rgb(255, 255, 255)); //white
                        if(flagEraserSegmentation)
                            myListSegmentation.changeModeTouch(2);
                        else
                            myListSegmentation.changeModeTouch(0);
                    }

                }
            }
        });
        //Add Point
        creatorPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation.animationScale(creatorPoints,TIME_ANIMATION,SCALE_ANIMATION,SCALE_ANIMATION);
                myListFigures.addPoint(layoutImageRx.getWidth()/2,layoutImageRx.getHeight()/2);
            }
        });
        //Add Circle
        creatorCircles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation.animationScale(creatorCircles,TIME_ANIMATION,SCALE_ANIMATION,SCALE_ANIMATION);
                myListFigures.addCircle(layoutImageRx.getWidth()/2,layoutImageRx.getHeight()/2,100);
            }
        });
        //Add Rectangle
        creatorRectangles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation.animationScale(creatorRectangles,TIME_ANIMATION,SCALE_ANIMATION,SCALE_ANIMATION);
                myListFigures.addRectangle(layoutImageRx.getWidth()/2-200,layoutImageRx.getHeight()/2-150,layoutImageRx.getWidth()/2+200,layoutImageRx.getHeight()/2+150);
            }
        });
        //Add Line
        creatorLines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation.animationScale(creatorLines,TIME_ANIMATION,SCALE_ANIMATION,SCALE_ANIMATION);
                myListFigures.addLine(layoutImageRx.getWidth()/2-200,layoutImageRx.getHeight()/2-150,layoutImageRx.getWidth()/2+200,layoutImageRx.getHeight()/2+150);
            }
        });
        //Add Ellipse
        creatorEllipses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation.animationScale(creatorEllipses,TIME_ANIMATION,SCALE_ANIMATION,SCALE_ANIMATION);
                myListFigures.addEllipse(layoutImageRx.getWidth()/2-200,layoutImageRx.getHeight()/2-150,layoutImageRx.getWidth()/2+200,layoutImageRx.getHeight()/2+150);
            }
        });
        //Delete Select Figure
        deleteFigures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation.animationScale(deleteFigures,TIME_ANIMATION,SCALE_ANIMATION,SCALE_ANIMATION);
                Toast toast;
                if(myListFigures.deleteFigure())
                    toast = Toast.makeText(getApplicationContext(),"Deleted Successfully",Toast.LENGTH_SHORT);
                else
                    toast = Toast.makeText(getApplicationContext(),"Isn't Selected Figure",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        //Menu of Change Colour of select figure
        changeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation.animationScale(changeColor,TIME_ANIMATION,SCALE_ANIMATION,SCALE_ANIMATION);
                    Animation animation1 = new TranslateAnimation(2, 0.0f, 2, -1.0f, 2, 0.0f, 2, 0.0f);
                    animation1.setDuration(750);
                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(android.view.animation.Animation animation) {
                        }
                        @Override
                        public void onAnimationEnd(android.view.animation.Animation animation) {
                            Animation animation2 = new TranslateAnimation(2, -1.0f, 2, 0.0f, 2, 0.0f, 2, 0.0f);
                            animation2.setDuration(750);
                            scrollLeft3.startAnimation(animation2);
                            scrollLeft1.setVisibility(View.GONE);
                            scrollLeft3.setVisibility(View.VISIBLE);
                            getFigures.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onAnimationRepeat(android.view.animation.Animation animation) {
                        }
                    });
                flagAnimationColors=false;
                    scrollLeft1.startAnimation(animation1);

            }
        });
        //Ocultar menu de Change colors
        iconColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = new TranslateAnimation(2, 0.0f, 2, -1.0f, 2, 0.0f, 2, 0.0f);
                animation1.setDuration(750);
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(android.view.animation.Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(android.view.animation.Animation animation) {
                        scrollLeft3.setVisibility(View.GONE);
                        if(flagSegmentation){
                            scrollLeft2.setVisibility(View.VISIBLE);
                            getFigures.setVisibility(View.GONE);
                            Animation animation2 = new TranslateAnimation(2, -1.0f, 2, 0.0f, 2, 0.0f, 2, 0.0f);
                            animation2.setDuration(750);
                            scrollLeft2.startAnimation(animation2);
                        }else {
                            scrollLeft1.setVisibility(View.VISIBLE);
                            getFigures.setVisibility(View.GONE);
                            Animation animation2 = new TranslateAnimation(2, -1.0f, 2, 0.0f, 2, 0.0f, 2, 0.0f);
                            animation2.setDuration(750);
                            scrollLeft1.startAnimation(animation2);
                        }

                    }
                    @Override
                    public void onAnimationRepeat(android.view.animation.Animation animation) {
                    }
                });
                scrollLeft3.startAnimation(animation1);
                flagAnimationColors=true;
            }
        });
        //change color all segments
        changeColorSegments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation.animationScale(changeColorSegments,TIME_ANIMATION,SCALE_ANIMATION,SCALE_ANIMATION);
                Animation animation1 = new TranslateAnimation(2, 0.0f, 2, -1.0f, 2, 0.0f, 2, 0.0f);
                animation1.setDuration(750);
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(android.view.animation.Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(android.view.animation.Animation animation) {
                        Animation animation2 = new TranslateAnimation(2, -1.0f, 2, 0.0f, 2, 0.0f, 2, 0.0f);
                        animation2.setDuration(750);
                        scrollLeft3.startAnimation(animation2);
                        scrollLeft2.setVisibility(View.GONE);
                        scrollLeft3.setVisibility(View.VISIBLE);
                        getFigures.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onAnimationRepeat(android.view.animation.Animation animation) {
                    }
                });
                flagAnimationColors=false;
                scrollLeft2.startAnimation(animation1);
                Toast toast;

            }
        });
        //xd
        pruebas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                //zoom al centro controles de container funcionan
                layoutImageRx.setScaleX(1.5f);
                layoutImageRx.setScaleY(1.5f);
                layoutImageRx.setTranslationX(10);
                layoutImageRx.setTranslationY(10);
                System.out.println("prueba SCALE -> x:"+layoutImageRx.getScaleX()+"; y:"+layoutImageRx.getScaleY());
                System.out.println("prueba PIVOT -> x:"+layoutImageRx.getPivotX()+"; y:"+layoutImageRx.getPivotY());
                System.out.println("prueba D -> x:"+myListFigures.dX()+"; y:"+myListFigures.dY());
                System.out.println("prueba TRASLATE -> x:"+layoutImageRx.getTranslationX()+"; y:"+layoutImageRx.getTranslationY());
                //
                layoutImageRx.setScaleX(1.0f);
                layoutImageRx.setScaleY(1.0f);
                layoutImageRx.setTranslationX(0);
                layoutImageRx.setTranslationY(0);
                //*/
                layoutImageRx.setPivotX(260);
                //layoutImageRx.setPivotY(100);
                layoutImageRx.setScaleX(1.3746202f);
                layoutImageRx.setScaleY(1.3746202f);
                layoutImageRx.setTranslationX(-0.57336426f);
                layoutImageRx.setTranslationY(-2.295105f);
                float dt = (layoutImageRx.getScaleX() * layoutImageRx.getWidth() - layoutImageRx.getWidth()) - layoutImageRx.getTranslationX() ;
                float dty=(layoutImageRx.getScaleY() * layoutImageRx.getHeight() - layoutImageRx.getHeight()) - layoutImageRx.getTranslationY();
                System.out.println("A prueba SCALE -> x:"+layoutImageRx.getScaleX()+"; y:"+layoutImageRx.getScaleY());
                System.out.println("A prueba PIVOT -> x:"+layoutImageRx.getPivotX()+"; y:"+layoutImageRx.getPivotY());
                System.out.println("A prueba D -> x:"+dt+"; y:"+dty);
                System.out.println("prueba TRASLATE -> x:"+layoutImageRx.getTranslationX()+"; y:"+layoutImageRx.getTranslationY());

            }
        });



        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(0);
            }
        });
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(1);
            }
        });
        color3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(2);
            }
        });
        color4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(3);
            }
        });
        color5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(4);
            }
        });
        color6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(5);
            }
        });
        color7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(6);
            }
        });
        color8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(7);
            }
        });
        color9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(8);
            }
        });
        color10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(9);
            }
        });
        color11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(10);
            }
        });
        color12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(11);
            }
        });
        color13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(12);
            }
        });
        color14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(13);
            }
        });
        color15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(14);
            }
        });
        color16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(15);
            }
        });
        color17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(16);
            }
        });
        color18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(17);
            }
        });
        color19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(18);
            }
        });
        color20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(19);
            }
        });
        color21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(20);
            }
        });
        color22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(21);
            }
        });
        color23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(22);
            }
        });
        color24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(23);
            }
        });
        color25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(24);
            }
        });
        //Delete the after segment
        deleteSegments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation.animationScale(deleteSegments,TIME_ANIMATION,SCALE_ANIMATION,SCALE_ANIMATION);
                myListSegmentation.after();
            }
        });

        //Layout forget touch
        zoomImageLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //Filter canny borders
        openCv.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterCanny());
            }
        });
        //Filter RGB is image original
        openCv3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(View v) {

                addFilter(myFilters.filterRGB());
            }
        });
        //Filter morph
        openCv2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                addFilter(myFilters.filterMorph());
            }
        });
        //Filter SEPIA
        openCv1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filerSepia());
            }
        });
        //filter summer
        openCv4.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        addFilter(myFilters.filterSummer());
           }
                     });
        //filter pink
        openCv5.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    addFilter(myFilters.filterPink());
        }
                  });
        //filter reduce colors gray
        openCv6.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterReduceColorsGray(5));
            }
        });
        //filters reduce Colors
        openCv7.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterReduceColors(80,15,10));
            }
        });
        //filter Pencil
        openCv8.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterPencil());
            }
        });
        //filter Carton
        openCv9.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterCarton(80,15,10));
            }
        });
        //filter R
        openCv10.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(0));
            }
        });
        openCv11.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(1));
            }
        });
        openCv12.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(2));
            }
        });
        openCv13.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(3));
            }
        });
        openCv14.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(4));
            }
        });
        openCv15.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(5));
            }
        });
        openCv16.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(7));
            }
        });
        openCv17.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(8));
            }
        });
        openCv18.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(9));
            }
        });
        openCv19.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(11));
            }
        });
        openCv20.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(12));
            }
        });
        openCv21.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(13));
            }
        });
        openCv22.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(14));
            }
        });
        openCv23.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(15));
            }
        });
        openCv24.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(16));
            }
        });
        openCv25.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(17));
            }
        });
        openCv26.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(18));
            }
        });
        openCv27.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(19));
            }
        });
        openCv28.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                addFilter(myFilters.filterColor(20));
            }
        });
        test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagAnimation) {
                    test1.animate().setDuration(200).scaleX(1.2f).scaleY(1.2f);
                    test1.setColorFilter(Color.rgb(255, 127, 80)); //ORANGE
                    Animation animation1 = new TranslateAnimation(2, 0.0f, 2, 1.0f, 2, 0.0f, 2, 0.0f);
                    animation1.setDuration(450);
                    menu_right.startAnimation(animation1);
                    Animation animation2 = new TranslateAnimation(2, 0.0f, 2, -1.0f, 2, 0.0f, 2, 0.0f);
                    animation2.setDuration(450);
                    menu_left.startAnimation(animation2);
                    if (scrollLeft3.getVisibility() == View.VISIBLE){
                        getFigures.startAnimation(animation2);
                        getFigures.setVisibility(View.GONE);
                    }
                    menu_right.setVisibility(View.GONE);
                    menu_left.setVisibility(View.GONE);
                    flagAnimation = false;
                }else{
                    test1.animate().setDuration(200).scaleX(1.0f).scaleY(1.0f);
                    test1.setColorFilter(Color.rgb(255, 255, 255)); //WHITE
                    Animation animation1 = new TranslateAnimation(2, 10.0f, 2, 0.0f, 2, 0.0f, 2, 0.0f);
                    animation1.setDuration(350);
                    menu_right.startAnimation(animation1);
                    Animation animation2 = new TranslateAnimation(2, -10.0f, 2, 0.0f, 2, 0.0f, 2, 0.0f);
                    animation2.setDuration(350);
                    if (scrollLeft3.getVisibility() == View.VISIBLE){
                        getFigures.startAnimation(animation2);
                        getFigures.setVisibility(View.VISIBLE);
                    }
                    menu_left.startAnimation(animation2);
                    menu_right.setVisibility(View.VISIBLE);
                    menu_left.setVisibility(View.VISIBLE);
                    flagAnimation = true;
                }
            }
        });
        import_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.chooser_dialog,null);
                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addFilter(Bitmap d){

        Drawable drawable = new BitmapDrawable(getResources(),d);
        if(!flagSegmentation)
            myListFigures.loadImage(d);
        else {
            myListSegmentation.loadImage(d);
            //myListSegmentation.changeFlagFilter(d);
            zoomImageLayout.setBackground(drawable);
        }
    }
    @SuppressLint("ShowToast")
    private void changeColor(int _color){
        Toast toast=Toast.makeText(getApplicationContext(),"Isn't Selected Figure",Toast.LENGTH_SHORT);
        if(flagSegmentation){
            if( myListSegmentation.changeColour(Util.getCollections()[_color]))
                toast = Toast.makeText(getApplicationContext(),"Change Successfully in Segments",Toast.LENGTH_SHORT);
        }else{
            if( myListFigures.changeColour(Util.getCollections()[_color]))
                toast = Toast.makeText(getApplicationContext(),"Change Successfully in Figure",Toast.LENGTH_SHORT);
        }
        toast.show();
    }
    /**
     * Method initial Properties Initializing Properties of Activity
     * */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initialProperties(){
        //properties about import image

        //Initializing properties--
        pruebas = findViewById(R.id.pruebas);
        extendsImage = findViewById(R.id.extendsImage);
        backFilters = findViewById(R.id.backFilters);
        iconColors = findViewById(R.id.figuresSet);
        filterImage = findViewById(R.id.filter);
        creatorCircles = findViewById(R.id.addCircle);
        creatorCircles.setColorFilter(Color.rgb(255,255,255));
        creatorRectangles = findViewById(R.id.addRectangle);
        creatorLines = findViewById(R.id.addLine);
        creatorEllipses = findViewById(R.id.addEllipse);
        creatorPoints = findViewById(R.id.addPoint);
        deleteFigures = findViewById(R.id.deleteFigures);
        changeColor = findViewById(R.id.changeColor);
        segmentation = findViewById(R.id.segmentation);
        eraserSegments = findViewById(R.id.eraserSegments);
        clearSegments = findViewById(R.id.clearSegments);
        menu_left = findViewById(R.id.contenedor_menu_left);
        menu_right = findViewById(R.id.contenedor_menu_right);
        menu_left.setBackgroundColor(Color.parseColor("#80000000"));
        menu_right.setBackgroundColor(Color.parseColor("#80000000"));
        getFigures = findViewById(R.id.getFigures);
        //ImageView load = findViewById(R.id.load);
        //ConstraintLayout frame = findViewById(R.id.frame);
        layoutImageRx = findViewById(R.id.layoutImageRx);
        myListFigures = new ListFigure(this,layoutImageRx);
        layoutImageRx.addView(myListFigures);
        //Animation
        Animation = new MyAnimation();
        //Scrolls
        scrollLeft1 = findViewById(R.id.scrollLeft_1);
        scrollLeft2 = findViewById(R.id.scrollLeft_2);
        scrollLeft3 = findViewById(R.id.scrollLeft_3);
        scrollRight1 = findViewById(R.id.scrollRight_1);
        scrollRight2 = findViewById(R.id.scrollRight_2);
        //Back and Checks
        backMenuRight = findViewById(R.id.back_menu_right_1);
        //Segmentation
        CardView cardView = findViewById(R.id.zoomImage_1); //CARD
        cardView.setVisibility(View.GONE);
        zoomImageLayout = findViewById(R.id.zoomLayoutImageRx_1); //Layout Zoom Image Ray-X
        zoomImageLayout.setVisibility(View.INVISIBLE);
        layoutImageRx1 = findViewById(R.id.layoutImageRx_1); //Layout Image Ray-X
        myListSegmentation = new ListSegmentation(this, zoomImageLayout, cardView,layoutImageRx1);
        layoutImageRx1.addView(myListSegmentation);
        deleteSegments = findViewById(R.id.deleteSegments);
        changeColorSegments = findViewById(R.id.changeColorSegments);
        //imports
        import_ = findViewById(R.id.import_);
        //Filters
        openCv = findViewById(R.id.openCV);
        openCv1 = findViewById(R.id.openCV1);
        openCv2 = findViewById(R.id.openCV2);
        openCv3 = findViewById(R.id.openCV3);
        openCv4 = findViewById(R.id.openCV4);
        openCv5 = findViewById(R.id.openCV5);
        openCv6 = findViewById(R.id.openCV6);
        openCv7 = findViewById(R.id.openCV7);
        openCv8 = findViewById(R.id.openCV8);
        openCv9 = findViewById(R.id.openCV9);
        openCv10 = findViewById(R.id.openCV10);
        openCv11 = findViewById(R.id.openCV11);
        openCv12 = findViewById(R.id.openCV12);
        openCv13 = findViewById(R.id.openCV13);
        openCv14 = findViewById(R.id.openCV14);
        openCv15 = findViewById(R.id.openCV15);
        openCv16 = findViewById(R.id.openCV16);
        openCv17 = findViewById(R.id.openCV17);
        openCv18 = findViewById(R.id.openCV18);
        openCv19 = findViewById(R.id.openCV19);
        openCv20 = findViewById(R.id.openCV20);
        openCv21 = findViewById(R.id.openCV21);
        openCv22 = findViewById(R.id.openCV22);
        openCv23 = findViewById(R.id.openCV23);
        openCv24 = findViewById(R.id.openCV24);
        openCv25 = findViewById(R.id.openCV25);
        openCv26 = findViewById(R.id.openCV26);
        openCv27 = findViewById(R.id.openCV27);
        openCv28 = findViewById(R.id.openCV28);

        //IMAGE
        test1 = findViewById(R.id.test);
        img = null;
        nameImage = -1;
        flagAnimation=true;
        flagAnimationColors=true;
        flagSegmentation = false;
        flagEraserSegmentation = false;
        try{
            nameImage = R.drawable.rx_image_5;
            img = loadResource(getApplicationContext(),nameImage);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            this.original = BitmapFactory.decodeResource(getResources(), R.drawable.rx_image_5, options);
        }catch (IOException e) {
            System.out.println("Insert image");
            e.printStackTrace();
        }
        myListFigures.loadImage(this.original);
        myListSegmentation.loadImage(this.original);
        myListSegmentation.setBackGround(this.original);
        myFilters = new MyFilters(this.img,this.original);
        //Icons with filter
        actualization_filters();
        //Icons Color
        listColors = new ArrayList<>();
        color1 = findViewById(R.id.color1); listColors.add(color1);
        color2 = findViewById(R.id.color2); listColors.add(color2);
        color3 = findViewById(R.id.color3); listColors.add(color3);
        color4 = findViewById(R.id.color4); listColors.add(color4);
        color5 = findViewById(R.id.color5); listColors.add(color5);
        color6 = findViewById(R.id.color6); listColors.add(color6);
        color7 = findViewById(R.id.color7); listColors.add(color7);
        color8 = findViewById(R.id.color8); listColors.add(color8);
        color9 = findViewById(R.id.color9); listColors.add(color9);
        color10 = findViewById(R.id.color10); listColors.add(color10);
        color11 = findViewById(R.id.color11); listColors.add(color11);
        color12 = findViewById(R.id.color12); listColors.add(color12);
        color13 = findViewById(R.id.color13); listColors.add(color13);
        color14 = findViewById(R.id.color14); listColors.add(color14);
        color15 = findViewById(R.id.color15); listColors.add(color15);
        color16 = findViewById(R.id.color16); listColors.add(color16);
        color17 = findViewById(R.id.color17); listColors.add(color17);
        color18 = findViewById(R.id.color18); listColors.add(color18);
        color19 = findViewById(R.id.color19); listColors.add(color19);
        color20 = findViewById(R.id.color20); listColors.add(color20);
        color21 = findViewById(R.id.color21); listColors.add(color21);
        color22 = findViewById(R.id.color22); listColors.add(color22);
        color23 = findViewById(R.id.color23); listColors.add(color23);
        color24 = findViewById(R.id.color24); listColors.add(color24);
        color25 = findViewById(R.id.color25); listColors.add(color25);
        //Add Filter Color
        for(int i = 0;i < listColors.size();i++){
            if(Util.getCollections()[i]!=null)
                listColors.get(i).setColorFilter(Color.rgb(Util.getCollections()[i][0],Util.getCollections()[i][1],Util.getCollections()[i][2]));
        }
        //--End Initializing
    }//End Method}
    public void import_galery(View view){
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,"Select picture"),PICK_IMAGE);
        Toast.makeText(getApplicationContext(),"image from galery",Toast.LENGTH_SHORT).show();

    }
    public void import_camera(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            Toast.makeText(getApplicationContext(),"image from camera",Toast.LENGTH_SHORT).show();
        }
    }
    public void import_galery_2(View view  ){
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,100);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == 100){
            Uri imgUri= data.getData();
            try{
                //BitmapFactory.Options options = new BitmapFactory.Options();
                //options.inScaled = false;

                String pad = getPath(imgUri);
                Toast.makeText(getApplicationContext(),pad,Toast.LENGTH_SHORT).show();
                loadImage(pad);
                displayImage(img);

            }catch (Exception e ){

            }
        }
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageurl = data.getData();
            try{
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                String pad = getPath(imageurl);
                
                Toast.makeText(getApplicationContext(),pad,Toast.LENGTH_SHORT).show();

              /*
                Bitmap map = BitmapFactory.decodeFile(pad,options);
                myListFigures.loadImage(map);
                myListSegmentation.loadImage(map);
                myListSegmentation.setBackGround(map);

                img = new Mat(map.getWidth(), map.getHeight(), CvType.CV_8UC3);
                Utils.bitmapToMat(map,img);
                myFilters.setImage(img,map);
                actualization_filters();
                */

            }catch (Exception e){
                System.out.print("adsasdasdasdasdasdads");
                e.printStackTrace();
                System.out.print(e.getMessage());
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            myListFigures.loadImage(imageBitmap);
            myListSegmentation.loadImage(imageBitmap);

            img = new Mat(imageBitmap.getWidth(), imageBitmap.getHeight(), CvType.CV_8UC3);
            Utils.bitmapToMat(imageBitmap,img);
            myFilters.setImage(img,imageBitmap);
            try {
                actualization_filters();
            }catch (Exception e){
                e.printStackTrace();
                System.out.print(e.getMessage());
            }


        }

    }

    private void displayImage(Mat mat) {
        Bitmap bitmap = Bitmap.createBitmap(mat.cols(),mat.rows(),Bitmap.Config.RGB_565);
        //convert map to bitmap

        Utils.matToBitmap(mat,bitmap);
        myListFigures.loadImage(bitmap);

    }

    private void loadImage(String pad) {
        img = Imgcodecs.imread(pad,Imgcodecs.CV_LOAD_IMAGE_COLOR); // image will be in rgb format
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Mat rgbimg = img.clone();
        Imgproc.cvtColor(rgbimg,rgbimg,Imgproc.COLOR_BGR2RGB);
        //this.original = BitmapFactory.decodeFile(pad);

        /*
        try {
            Imgproc.cvtColor(originalImage, rgbimg,Imgproc.COLOR_BGR2RGB);
            Display display = getWindowManager().getDefaultDisplay();

            android.graphics.Point size = new android.graphics.Point();
            display.getSize(size);

            int mobilwitdth = size.x;
            int mobilheigth = size.y;
            sampleImg = new Mat();
            double downSampleRatio = calculateSubSimpleSize(rgbimg, mobilwitdth, mobilheigth);

            Imgproc.resize(rgbimg, sampleImg, new Size(), downSampleRatio, downSampleRatio, Imgproc.INTER_AREA);

            try {
                ExifInterface exif = new ExifInterface(pad);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        sampleImg = sampleImg.t();
                        Core.flip(sampleImg, sampleImg, 1);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        sampleImg = sampleImg.t();
                        Core.flip(sampleImg, sampleImg, 0);
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

*/

    }

    private double calculateSubSimpleSize(Mat scr, int mobilwitdth, int mobilheigth) {
     final int width = scr.width();
     final int heigth = scr.height();
     double insampleSize = -1;

     if(heigth>mobilheigth ||width > mobilwitdth){

         final  double heigthRatio = (double) mobilheigth/(double)heigth;
         final  double widthRatio = (double)mobilwitdth/(double)width;

         insampleSize = heigthRatio < widthRatio ? heigth:width;
     }
     return insampleSize;
    }

    private String getPath(Uri imgUri) {
        if(imgUri == null){
            return "aa";
        }else{
            String []projection= {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imgUri, projection,null,null,null);
            if(cursor != null){
                int col_index =  cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(col_index);
            }
            else{
                return "bb";
            }

        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void actualization_filters(){
        openCv.setImageBitmap(myFilters.cropBitmap(myFilters.filterCanny()));
        openCv1.setImageBitmap(myFilters.cropBitmap(myFilters.filerSepia()));
        openCv2.setImageBitmap(myFilters.cropBitmap(myFilters.filterMorph()));
        openCv3.setImageBitmap(myFilters.cropBitmap(myFilters.filterRGB()));
      //  openCv4.setImageBitmap(myFilters.cropBitmap(myFilters.filterSummer()));
      //  openCv5.setImageBitmap(myFilters.cropBitmap(myFilters.filterPink()));
        //openCv6.setImageBitmap(myFilters.cropBitmap(myFilters.filterReduceColorsGray(5)));
        //openCv7.setImageBitmap(myFilters.cropBitmap(myFilters.filterReduceColors(80,15,10)));
        //openCv8.setImageBitmap(myFilters.cropBitmap(myFilters.filterPencil()));
        //openCv9.setImageBitmap(myFilters.cropBitmap(myFilters.filterCarton(80,15,10)));
        /*
        openCv10.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(0)));
        openCv11.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(1)));
        openCv12.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(2)));
        openCv13.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(3)));
        openCv14.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(4)));
        openCv15.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(5)));
        openCv16.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(7)));
        openCv17.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(8)));
        openCv18.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(9)));
        openCv19.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(11)));
        openCv20.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(12)));
        openCv21.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(13)));
        openCv22.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(14)));
        openCv23.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(15)));
        openCv24.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(16)));
        openCv25.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(17)));
        openCv26.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(18)));
        openCv27.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(19)));
        openCv28.setImageBitmap(myFilters.cropBitmap(myFilters.filterColor(20)));
    */
    }


}//End Class
