/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.core;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ethink.face.FaceException;
import com.ethink.face.core.arc.AFD_FSDKLibrary;
import com.ethink.face.core.arc.AFD_FSDK_FACERES;
import com.ethink.face.core.arc.AFD_FSDK_Version;
import com.ethink.face.core.arc.AFR_FSDKLibrary;
import com.ethink.face.core.arc.AFR_FSDK_FACEINPUT;
import com.ethink.face.core.arc.AFR_FSDK_FACEMODEL;
import com.ethink.face.core.arc.AFR_FSDK_Version;
import com.ethink.face.core.arc.ASVLOFFSCREEN;
import com.ethink.face.core.arc.ASVL_COLOR_FORMAT;
import com.ethink.face.core.arc.BufferInfo;
import com.ethink.face.core.arc.CLibrary;
import com.ethink.face.core.arc.FaceInfo;
import com.ethink.face.core.arc.ImageLoader;
import com.ethink.face.core.arc.MRECT;
import com.ethink.face.core.arc._AFD_FSDK_OrientPriority;
import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 *
 * 描述: 人脸识别核心引擎
 * @author wangjing.dc@qq.com
 */
@Component
public class FaceEnginer {

	/**
	 * 1.0 1:1 比对
	 *  64CAZFZEtym2DHzHJ3HenUhAW5YLTeMoZVg1WHiaY3Tq
	 * 人脸追踪(FT):6f5VAuVjMDuKexSvC3TxHvXGoZGWoKMwP6VW59cC6d9q
	 * 人脸检测(FD):6f5VAuVjMDuKexSvC3TxHvXPxxXk9SK6QZVgm39jQ417
	 * 人脸识别(FR):6f5VAuVjMDuKexSvC3TxHvXX8MnsGA2YzXTzcRLv8kVG
	 */

	/**
	 * 1.1 版本 1:1比对 
	 * APP_Id:64CAZFZEtym2DHzHJ3HenUhQpt4gLHXDnEu7o2MWiAHh
	 * 人脸追踪(FT):57AsqW8PR5xUbbTWJXDeFY1w2mnmam9k2v2xTo5VeT1q
	 * 人脸检测(FD):57AsqW8PR5xUbbTWJXDeFY24CB3wtMUXw53MUhrGCgfz
	 * 人脸识别(FR):57AsqW8PR5xUbbTWJXDeFY2BMaK7ddvz5az2jYaP6r9k
	 * 年龄识别(Age):57AsqW8PR5xUbbTWJXDeFY2oAacyb7v6dk98gn65PAwB
	 * 性别识别(Gender):57AsqW8PR5xUbbTWJXDeFY2vKyt63kSZ2CEJsTJ9S4i4
	 */

	/**
	 * 1：N 比对引擎，中型网络，支持2000人 
	 * APP_Id 64CAZFZEtym2DHzHJ3HenUhXzHKrqsumq92W4d2t7b1v
	 * SDK_key: 人脸追踪(FT) Key C9AA7AgXgFDoz8iurt9L8P1SszqbUrSv8cksbmhzhHFG 
	 * 人脸检测(FD)Key C9AA7AgXgFDoz8iurt9L8P1a3Q6ppDuq8Sw82aLqh9KW 
	 * 人脸识别(FR) Key C9AA7AgXgFDoz8iurt9L8P24h19VQeZ1ijQtLFAJLpNK 
	 * 年龄识别(Age) Key C9AA7AgXgFDoz8iurt9L8P2K1ofrCmJQjbjUYbz4mZqP 
	 * 性别识别(Gender) Key C9AA7AgXgFDoz8iurt9L8P2SBCvxogrAfDDQCNAFuTnk
	 */

	private final String APPID         = "64CAZFZEtym2DHzHJ3HenUhXzHKrqsumq92W4d2t7b1v";
	private final String FD_SDKKEY = "C9AA7AgXgFDoz8iurt9L8P1a3Q6ppDuq8Sw82aLqh9KW";
	private final String FR_SDKKEY = "C9AA7AgXgFDoz8iurt9L8P24h19VQeZ1ijQtLFAJLpNK";
	
	private final int FD_WORKBUF_SIZE = 21 * 1024 * 1024;
	private final int FR_WORKBUF_SIZE = 41 * 1024 * 1024;
	private final int MAX_FACE_NUM = 50;

	private final boolean bUseBGRToEngine = true;
    
    private static final Logger log = LoggerFactory.getLogger(FaceEnginer.class);
    
    // 引擎指针
    private Pointer hFDEngine;
    private Pointer hFREngine;
    private Pointer pFDWorkMem;
    private Pointer pFRWorkMem;
    
    /**
     * 初始化引擎
     */
    @PostConstruct
    public void initEnginer() {
    	log.info("*****************************初始化引擎*************************************");
        // init Engine
        pFDWorkMem = CLibrary.INSTANCE.malloc(FD_WORKBUF_SIZE);
        pFRWorkMem = CLibrary.INSTANCE.malloc(FR_WORKBUF_SIZE);

        PointerByReference phFDEngine = new PointerByReference();
        NativeLong ret = AFD_FSDKLibrary.INSTANCE.AFD_FSDK_InitialFaceEngine(APPID, FD_SDKKEY,
        		pFDWorkMem, FD_WORKBUF_SIZE, phFDEngine, 
        		_AFD_FSDK_OrientPriority.AFD_FSDK_OPF_0_HIGHER_EXT, 
        		32, MAX_FACE_NUM);
        if (ret.longValue() != 0) {
            CLibrary.INSTANCE.free(pFDWorkMem);
            CLibrary.INSTANCE.free(pFRWorkMem);
            log.error(String.format("AFD_FSDK_InitialFaceEngine ret 0x%x",ret.longValue()));
            throw new RuntimeException("初始AFD化引擎失败");
        }

        // print FDEngine version
        hFDEngine = phFDEngine.getValue();
        AFD_FSDK_Version versionFD = AFD_FSDKLibrary.INSTANCE.AFD_FSDK_GetVersion(hFDEngine);
        log.info(String.format("%d %d %d %d", versionFD.lCodebase, versionFD.lMajor, versionFD.lMinor, versionFD.lBuild));
        log.info(versionFD.Version.replaceAll("ArcSoft_Free", ""));
        log.info(versionFD.BuildDate);
        log.info(versionFD.CopyRight.replaceAll("ArcSoft", ""));

        PointerByReference phFREngine = new PointerByReference();
        ret = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_InitialEngine(APPID, FR_SDKKEY, pFRWorkMem, FR_WORKBUF_SIZE, phFREngine);
        if (ret.longValue() != 0) {
            AFD_FSDKLibrary.INSTANCE.AFD_FSDK_UninitialFaceEngine(hFDEngine);
            CLibrary.INSTANCE.free(pFDWorkMem);
            CLibrary.INSTANCE.free(pFRWorkMem);
            log.error(String.format("AFR_FSDK_InitialEngine ret 0x%x" ,ret.longValue()));
            throw new RuntimeException("初始化AFR引擎失败");
        }

        // print FREngine version
        hFREngine = phFREngine.getValue();
        AFR_FSDK_Version versionFR = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_GetVersion(hFREngine);
        log.info(String.format("%d %d %d %d", versionFR.lCodebase, versionFR.lMajor, versionFR.lMinor, versionFR.lBuild));
        log.info(versionFR.Version.replaceAll("ArcSoft_Free", ""));
        log.info(versionFR.BuildDate);
        log.info(versionFR.CopyRight.replaceAll("ArcSoft", ""));
        log.info("*****************************初始化引擎完成*************************************");
    }
    
    /**
     * 释放引擎
     */
    @PreDestroy
    public void destroyEngine() {
    	// release Engine
        AFD_FSDKLibrary.INSTANCE.AFD_FSDK_UninitialFaceEngine(hFDEngine);
        AFR_FSDKLibrary.INSTANCE.AFR_FSDK_UninitialEngine(hFREngine);

        CLibrary.INSTANCE.free(pFDWorkMem);
        CLibrary.INSTANCE.free(pFRWorkMem);
    }
    
    /**
     * 计算图片相似度
     * @param filePathA
     * @param filePathB
     * @return 
     */
    public float similar(String filePathA, String filePathB) throws FaceException {
        // load Image Data
        ASVLOFFSCREEN  inputImgA = loadImage(filePathA);
        ASVLOFFSCREEN  inputImgB = loadImage(filePathB);

        float value = compareFaceSimilarity(hFDEngine, hFREngine, inputImgA, inputImgB);
        log.info(String.format("similarity between faceA and faceB is %f" , value));        
        return value;
    }
    
    /**
     * 计算两组特征值相似度
     * @param featurea 人脸特征值a 
     * @param featureb 人脸特征值b
     * @return
     * @throws FaceException 
     */
	public float similarByFeature(byte[] featurea, byte[] featureb) throws FaceException {
		// 加载人脸特征
		AFR_FSDK_FACEMODEL fa;
		AFR_FSDK_FACEMODEL fb;
		try {
			fa = AFR_FSDK_FACEMODEL.fromByteArray(featurea);
			fb = AFR_FSDK_FACEMODEL.fromByteArray(featureb);
		} catch (Exception e) {
			log.error("人脸特征数据加载失败", e);
			throw new FaceException(FaceException.ENGINER_ERROR, "人脸特征数据加载失败");
		}
		

		// calc similarity between faceA and faceB
		FloatByReference fSimilScore = new FloatByReference(0.0f);
		NativeLong ret = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_FacePairMatching(hFREngine, fa, fb, fSimilScore);
		fa.freeUnmanaged();
		fb.freeUnmanaged();

		if (ret.longValue() != 0) {
			throw new FaceException(FaceException.ENGINER_ERROR,
					String.format("AFR_FSDK_FacePairMatching failed:ret 0x%x", ret.longValue()));
		}
		return fSimilScore.getValue();
	}
        
    /**
     * 提取人脸特征
     * @param imgPath 人脸图片路径
     * @return 人脸特征值
     * @throws FaceException 
     */
	public byte[] extractFaceFeature(String imgPath) throws FaceException {
		ASVLOFFSCREEN inputImgA = loadImage(imgPath);

		// Do Face Detect
		FaceInfo[] faceInfos = doFaceDetection(hFDEngine, inputImgA);
		if (faceInfos.length < 1) {
			log.error("imgPath=" + imgPath);
			throw new FaceException(FaceException.ENGINER_ERROR, "face未检测到人脸");
		}

		// Extract Face Feature
		AFR_FSDK_FACEMODEL faceFeatureA = extractFRFeature(hFREngine, inputImgA, faceInfos[0]);
		if (faceFeatureA == null) {
			throw new FaceException(FaceException.ENGINER_ERROR, "计算face人脸特征失败");
		}

		try {
			byte[] ret = faceFeatureA.toByteArray();
			faceFeatureA.freeUnmanaged();
			return ret;
		} catch (Exception e) {
			throw new FaceException(FaceException.ENGINER_ERROR, "人脸特征转换失败");
		}
		
	}
	
	/**
     * 人脸检测
     * @param hFDEngine 引擎 
     * @param inputImg 待检测图片
     * @return
     */
    private FaceInfo[] doFaceDetection(Pointer hFDEngine, ASVLOFFSCREEN inputImg) {
        FaceInfo[] faceInfo = new FaceInfo[0];

        PointerByReference ppFaceRes = new PointerByReference();
        NativeLong ret = AFD_FSDKLibrary.INSTANCE.AFD_FSDK_StillImageFaceDetection(hFDEngine, inputImg, ppFaceRes);
        if (ret.longValue() != 0) {
            log.info(String.format("AFD_FSDK_StillImageFaceDetection ret 0x%x" , ret.longValue()));
            return faceInfo;
        }

        AFD_FSDK_FACERES faceRes = new AFD_FSDK_FACERES(ppFaceRes.getValue());
        if (faceRes.nFace > 0) {
            faceInfo = new FaceInfo[faceRes.nFace];
            for (int i = 0; i < faceRes.nFace; i++) {
                MRECT rect = new MRECT(new Pointer(Pointer.nativeValue(faceRes.rcFace.getPointer()) + faceRes.rcFace.size() * i));
                int orient = faceRes.lfaceOrient.getPointer().getInt(i * 4);
                faceInfo[i] = new FaceInfo();

                faceInfo[i].left = rect.left;
                faceInfo[i].top = rect.top;
                faceInfo[i].right = rect.right;
                faceInfo[i].bottom = rect.bottom;
                faceInfo[i].orient = orient;

                log.info(String.format("检测到人脸: %d (%d %d %d %d) orient %d", i, rect.left, rect.top, rect.right, rect.bottom, orient));
            }
        }
        return faceInfo;
    }

    /**
     * 提取人脸特征
     * @param hFREngine
     * @param inputImg
     * @param faceInfo
     * @return
     */
    private AFR_FSDK_FACEMODEL extractFRFeature(Pointer hFREngine, ASVLOFFSCREEN inputImg, FaceInfo faceInfo) {

        AFR_FSDK_FACEINPUT faceinput = null;
        try{
        	faceinput = new AFR_FSDK_FACEINPUT();
        }catch(Exception e) {
        	e.printStackTrace();
        }
        faceinput.lOrient = faceInfo.orient;
        faceinput.rcFace.left = faceInfo.left;
        faceinput.rcFace.top = faceInfo.top;
        faceinput.rcFace.right = faceInfo.right;
        faceinput.rcFace.bottom = faceInfo.bottom;

        AFR_FSDK_FACEMODEL faceFeature = new AFR_FSDK_FACEMODEL();
        NativeLong ret = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_ExtractFRFeature(hFREngine, inputImg, faceinput, faceFeature);
        if (ret.longValue() != 0) {
            System.out.println(String.format("AFR_FSDK_ExtractFRFeature ret 0x%x" ,ret.longValue()));
            return null;
        }

        try {
            return faceFeature.deepCopy();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 核心人脸相似度比对 
     * @param hFDEngine 
     * @param hFREngine
     * @param inputImgA
     * @param inputImgB
     * @return
     * @throws FaceException 
     */
	private float compareFaceSimilarity(Pointer hFDEngine, Pointer hFREngine, ASVLOFFSCREEN inputImgA,
			ASVLOFFSCREEN inputImgB) throws FaceException {
		// Do Face Detect
		FaceInfo[] faceInfosA = doFaceDetection(hFDEngine, inputImgA);
		if (faceInfosA.length < 1) {
			throw new FaceException(FaceException.ENGINER_ERROR, "facea未检测到人脸");
		}

		FaceInfo[] faceInfosB = doFaceDetection(hFDEngine, inputImgB);
		if (faceInfosB.length < 1) {
			throw new FaceException(FaceException.ENGINER_ERROR, "faceb未检测到人脸");
		}

		// Extract Face Feature
		AFR_FSDK_FACEMODEL faceFeatureA = extractFRFeature(hFREngine, inputImgA, faceInfosA[0]);
		if (faceFeatureA == null) {
			throw new FaceException(FaceException.ENGINER_ERROR, "计算facea人脸特征失败");
		}

		AFR_FSDK_FACEMODEL faceFeatureB = extractFRFeature(hFREngine, inputImgB, faceInfosB[0]);
		if (faceFeatureB == null) {
			faceFeatureA.freeUnmanaged();			
			throw new FaceException(FaceException.ENGINER_ERROR, "计算faceb人脸特征失败");
		}

		// calc similarity between faceA and faceB
		FloatByReference fSimilScore = new FloatByReference(0.0f);
		NativeLong ret = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_FacePairMatching(hFREngine, faceFeatureA, faceFeatureB,
				fSimilScore);
		faceFeatureA.freeUnmanaged();
		faceFeatureB.freeUnmanaged();
		if (ret.longValue() != 0) {
			throw new FaceException(FaceException.ENGINER_ERROR, 
					String.format("AFR_FSDK_FacePairMatching failed:ret 0x%x", ret.longValue()));
		}
		return fSimilScore.getValue();
	}

    /**
     * 加载人脸图片
     * @param filePath
     * @return
     */
    private ASVLOFFSCREEN loadImage(String filePath) throws FaceException{
        ASVLOFFSCREEN inputImg = new ASVLOFFSCREEN();

        try {
        
	        if (bUseBGRToEngine) {
	            BufferInfo bufferInfo = ImageLoader.getBGRFromFile(filePath);
	            inputImg.u32PixelArrayFormat = ASVL_COLOR_FORMAT.ASVL_PAF_RGB24_B8G8R8;
	            inputImg.i32Width = bufferInfo.width;
	            inputImg.i32Height = bufferInfo.height;
	            inputImg.pi32Pitch[0] = inputImg.i32Width * 3;
	            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
	            inputImg.ppu8Plane[0].write(0, bufferInfo.buffer, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
	            inputImg.ppu8Plane[1] = Pointer.NULL;
	            inputImg.ppu8Plane[2] = Pointer.NULL;
	            inputImg.ppu8Plane[3] = Pointer.NULL;
	        } else {
	            BufferInfo bufferInfo = ImageLoader.getI420FromFile(filePath);
	            inputImg.u32PixelArrayFormat = ASVL_COLOR_FORMAT.ASVL_PAF_I420;
	            inputImg.i32Width = bufferInfo.width;
	            inputImg.i32Height = bufferInfo.height;
	            inputImg.pi32Pitch[0] = inputImg.i32Width;
	            inputImg.pi32Pitch[1] = inputImg.i32Width / 2;
	            inputImg.pi32Pitch[2] = inputImg.i32Width / 2;
	            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
	            inputImg.ppu8Plane[0].write(0, bufferInfo.buffer, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
	            inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
	            inputImg.ppu8Plane[1].write(0, bufferInfo.buffer, inputImg.pi32Pitch[0] * inputImg.i32Height, inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
	            inputImg.ppu8Plane[2] = new Memory(inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
	            inputImg.ppu8Plane[2].write(0, bufferInfo.buffer, inputImg.pi32Pitch[0] * inputImg.i32Height + inputImg.pi32Pitch[1] * inputImg.i32Height / 2, inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
	            inputImg.ppu8Plane[3] = Pointer.NULL;
	        }
	
	        inputImg.setAutoRead(false);
	        return inputImg;
        
        }catch(Throwable e) {
        	log.error(null, e);
        	throw new FaceException(FaceException.ENGINER_ERROR, "读取图片失败");
        }
    }

    public static void main(String[] args) {
    	try {
    		String a = "D:\\etface\\tmps\\00100048photo2.jpg";
    		String b = "D:\\etface\\tmps\\00100059photo2.jpg";
    		
    		FaceEnginer fe = new FaceEnginer();
    		fe.initEnginer();
    		long start = System.currentTimeMillis();
    		System.out.println("-----------------"+fe.similar(a, b));
    		System.out.println("1111111111=" + (System.currentTimeMillis() - start));
			
    		byte[] ab = fe.extractFaceFeature(a);
//    		System.out.println(Arrays.toString(ab));
//    		System.out.println(ab.length);
    		
    		byte[] bb = fe.extractFaceFeature(b);
    		FileUtils.writeByteArrayToFile(new File("e:/datatmp.byte"), bb);
    		
    		start = System.currentTimeMillis();
    		bb = FileUtils.readFileToByteArray(new File("e:/datatmp.byte"));
    		System.out.println(fe.similarByFeature(ab, bb));
    		System.out.println("222222=" + (System.currentTimeMillis() - start));
    		
    		
    		
			fe.destroyEngine();
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }
    
    

}
