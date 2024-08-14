package com.pnuppp.pplusplus;

import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.MapLifeCycleCallback;
import com.kakao.vectormap.MapOverlay;
import com.kakao.vectormap.MapView;
import com.kakao.vectormap.MapType;
import com.kakao.vectormap.MapViewInfo;
import com.kakao.vectormap.camera.CameraUpdate;
import com.kakao.vectormap.camera.CameraUpdateFactory;
import com.kakao.vectormap.label.Label;
import com.kakao.vectormap.label.LabelLayer;
import com.kakao.vectormap.label.LabelManager;
import com.kakao.vectormap.label.LabelOptions;
import com.kakao.vectormap.label.LabelStyle;
import com.kakao.vectormap.label.LabelStyles;

public class newnewActivity extends AppCompatActivity {

    MapView mapView;
    KakaoMap kakaoMap;
    LabelManager labelManager; // LabelManager를 클래스 변수로 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_newnew);
        mapView = findViewById(R.id.map_view);

        mapView.start(new MapLifeCycleCallback() {
            @Override
            public void onMapDestroy() {
                // 지도 API가 정상적으로 종료될 때 호출
                Log.d("KakaoMap", "onMapDestroy: ");
            }

            @Override
            public void onMapError(Exception error) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출
                Log.e("KakaoMap", "onMapError: ", error);
            }
        }, new KakaoMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull KakaoMap map) {
                // 정상적으로 인증이 완료되었을 때 호출
                kakaoMap = map;

                // LabelManager 초기화
                labelManager = kakaoMap.getLabelManager();

                // 지도 초기 위치 설정
                cameraupdate();

                // 지도 타입 변경
                changeMapViewInfo();

                // 로드뷰 라인 오버레이 표시
                showRoadViewLineOverlay();

                // 레이블 표시
                showLabel();
            }
        });
    }

    private void cameraupdate() {
        // 카메라 업데이트 객체 생성
        // 원하는 위치
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(35.2304389, 129.0842777));

        // 카메라 이동
        kakaoMap.moveCamera(cameraUpdate);

        CameraUpdate cameraUpdate2 = CameraUpdateFactory.zoomTo(17);;

        // 카메라 이동
        kakaoMap.moveCamera(cameraUpdate2);
    }

    private void changeMapViewInfo() {
        // MapViewInfo 변경 리스너 설정
        kakaoMap.setOnMapViewInfoChangeListener(new KakaoMap.OnMapViewInfoChangeListener() {
            @Override
            public void onMapViewInfoChanged(MapViewInfo mapViewInfo) {
                // MapViewInfo 변경 성공 시 호출
                Log.d("KakaoMap", "MapViewInfo changed successfully");
            }

            @Override
            public void onMapViewInfoChangeFailed() {
                // MapViewInfo 변경 실패 시 호출
                Log.e("KakaoMap", "Failed to change MapViewInfo");
            }
        });

        // MapViewInfo 객체 생성 및 지도 타입을 위성 지도(SKYVIEW)로 변경
        MapViewInfo mapViewInfo = MapViewInfo.from("openmap", MapType.NORMAL);
        kakaoMap.changeMapViewInfo(mapViewInfo);
    }

    private void showRoadViewLineOverlay() {
        // MapOverlay 로 로드뷰 라인 오버레이 켜기
        kakaoMap.showOverlay(MapOverlay.ROADVIEW_LINE);
    }

    private void hideRoadViewLineOverlay() {
        // MapOverlay 로 로드뷰 라인 오버레이 끄기
        kakaoMap.hideOverlay(MapOverlay.ROADVIEW_LINE);
    }

    private void showLabel() {
        // 1. 첫 번째 레이블 스타일 생성 - Icon 이미지 하나만 있는 스타일
        LabelStyles firstLabelStyles = LabelStyles.from(LabelStyle.from(R.drawable.location));

        // 2. 첫 번째 레이블 옵션 생성
        LabelOptions firstLabelOptions = LabelOptions.from(LatLng.from(35.2304389, 129.0842777))
                .setStyles(firstLabelStyles);

        // 3. 첫 번째 레이블 추가
        LabelLayer layer = labelManager.getLayer();
        Label firstLabel = layer.addLabel(firstLabelOptions);

        // 4. 두 번째 레이블 스타일 생성 - 다른 아이콘 이미지 스타일
        LabelStyles secondLabelStyles = LabelStyles.from(LabelStyle.from(R.drawable.location)); // 새로운 아이콘 이미지

        // 5. 두 번째 레이블 옵션 생성
        LabelOptions secondLabelOptions = LabelOptions.from(LatLng.from(35.2301701, 129.085188)) // 다른 위치
                .setStyles(secondLabelStyles);

        // 6. 두 번째 레이블 추가
        Label secondLabel = layer.addLabel(secondLabelOptions);
//버거킹
        LabelStyles LabelStyles3 = LabelStyles.from(LabelStyle.from(R.drawable.location)); // 새로운 아이콘 이미지


        LabelOptions LabelOptions3 = LabelOptions.from(LatLng.from(35.230478, 129.0866165)) // 다른 위치
                .setStyles(secondLabelStyles);


        Label Label3 = layer.addLabel(LabelOptions3);
//미분당
        LabelStyles LabelStyles4 = LabelStyles.from(LabelStyle.from(R.drawable.location)); // 새로운 아이콘 이미지


        LabelOptions LabelOptions4 = LabelOptions.from(LatLng.from(35.230273, 129.0851434)) // 다른 위치
                .setStyles(secondLabelStyles);


        Label Label4 = layer.addLabel(LabelOptions4);

//웍헤이
        LabelStyles LabelStyles5 = LabelStyles.from(LabelStyle.from(R.drawable.location)); // 새로운 아이콘 이미지


        LabelOptions LabelOptions5 = LabelOptions.from(LatLng.from(35.228884, 129.0880355)) // 다른 위치
                .setStyles(secondLabelStyles);


        Label Label5 = layer.addLabel(LabelOptions5);

//포포포
        LabelStyles LabelStyles6 = LabelStyles.from(LabelStyle.from(R.drawable.location)); // 새로운 아이콘 이미지


        LabelOptions LabelOptions6 = LabelOptions.from(LatLng.from(35.231564, 129.085253)) // 다른 위치
                .setStyles(secondLabelStyles);


        Label Label6 = layer.addLabel(LabelOptions6);

//카츠안
        LabelStyles LabelStyles7 = LabelStyles.from(LabelStyle.from(R.drawable.location)); // 새로운 아이콘 이미지


        LabelOptions LabelOptions7 = LabelOptions.from(LatLng.from(35.2301375, 129.0857935)) // 다른 위치
                .setStyles(secondLabelStyles);


        Label Label7 = layer.addLabel(LabelOptions7);

//야마벤또
        LabelStyles LabelStyles8 = LabelStyles.from(LabelStyle.from(R.drawable.location)); // 새로운 아이콘 이미지


        LabelOptions LabelOptions8 = LabelOptions.from(LatLng.from(35.2310302, 129.084811)) // 다른 위치
                .setStyles(secondLabelStyles);


        Label Label8 = layer.addLabel(LabelOptions8);

//금정회관
        LabelStyles LabelStyles9 = LabelStyles.from(LabelStyle.from(R.drawable.location)); // 새로운 아이콘 이미지


        LabelOptions LabelOptions9 = LabelOptions.from(LatLng.from(35.23346110000001, 129.0803809)) // 다른 위치
                .setStyles(secondLabelStyles);


        Label Label9 = layer.addLabel(LabelOptions9);

        //교토밥상


        LabelStyles LabelStyles10 = LabelStyles.from(LabelStyle.from(R.drawable.location)); // 새로운 아이콘 이미지


        LabelOptions LabelOptions10 = LabelOptions.from(LatLng.from(35.2320819, 129.0854509)) // 다른 위치
                .setStyles(secondLabelStyles);


        Label Label10 = layer.addLabel(LabelOptions10);
    }
}
