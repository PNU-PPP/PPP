package com.pnuppp.pplusplus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class newnewActivity extends AppCompatActivity {

    // enum을 사용하는 클래스 내부에 정의
    public enum RestaurantType {
        PIZZA, SUSHI, KOREAN, CHINESE, VIETNAM
    }

    MapView mapView;
    KakaoMap kakaoMap;
    LabelManager labelManager; // LabelManager를 클래스 변수로 선언
    List<Label> labelList = new ArrayList<>(); // 레이블들을 저장할 리스트
    Map<Label, RestaurantInfo> labelToRestaurantMap = new HashMap<>(); // 레이블과 식당 정보를 매핑할 Map
    Button selectLabelButton; // 랜덤으로 레이블을 선택할 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_newnew);
        mapView = findViewById(R.id.map_view);

        // 버튼 초기화
        selectLabelButton = findViewById(R.id.select_label_button);
        selectLabelButton.setOnClickListener(v -> selectRandomLabel());

        mapView.start(new MapLifeCycleCallback() {
            @Override
            public void onMapDestroy() {
                Log.d("KakaoMap", "onMapDestroy: ");
            }

            @Override
            public void onMapError(Exception error) {
                Log.e("KakaoMap", "onMapError: ", error);
            }
        }, new KakaoMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull KakaoMap map) {
                kakaoMap = map;
                labelManager = kakaoMap.getLabelManager();
                cameraupdate();
                changeMapViewInfo();
                showRoadViewLineOverlay();
                showLabel();
            }
        });
    }

    private void cameraupdate() {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(35.2304389, 129.0842777));
        kakaoMap.moveCamera(cameraUpdate);

        CameraUpdate cameraUpdate2 = CameraUpdateFactory.zoomTo(17);
        kakaoMap.moveCamera(cameraUpdate2);
    }

    private void changeMapViewInfo() {
        kakaoMap.setOnMapViewInfoChangeListener(new KakaoMap.OnMapViewInfoChangeListener() {
            @Override
            public void onMapViewInfoChanged(MapViewInfo mapViewInfo) {
                Log.d("KakaoMap", "MapViewInfo changed successfully");
            }

            @Override
            public void onMapViewInfoChangeFailed() {
                Log.e("KakaoMap", "Failed to change MapViewInfo");
            }
        });

        MapViewInfo mapViewInfo = MapViewInfo.from("openmap", MapType.NORMAL);
        kakaoMap.changeMapViewInfo(mapViewInfo);
    }

    private void showRoadViewLineOverlay() {
        kakaoMap.showOverlay(MapOverlay.ROADVIEW_LINE);
    }

    private void hideRoadViewLineOverlay() {
        kakaoMap.hideOverlay(MapOverlay.ROADVIEW_LINE);
    }

    private void showLabel() {
        LabelLayer layer = labelManager.getLayer();

        addLabel(layer, 35.2304389, 129.0842777, "톤쇼우", "부산대 근본 카츠", "https://g.co/kgs/F6ZsNBX", RestaurantType.SUSHI);
        addLabel(layer, 35.2301701, 129.085188, "마마도마", "비싸지만 맛있는 스시", "https://g.co/kgs/eHQ3FQL", RestaurantType.VIETNAM);
        addLabel(layer, 35.230478, 129.0866165, "버거킹", "세계적인 패스트푸드 체인", "https://g.co/kgs/DUL2e2F", RestaurantType.PIZZA);
        addLabel(layer, 35.230273, 129.0851434, "미분당", "조용한 쌀국수", "https://g.co/kgs/MyeJqTd", RestaurantType.SUSHI);
        addLabel(layer, 35.228884, 129.0880355, "웍헤이", "볶음밥 맛도리", "https://g.co/kgs/QcmgSLD", RestaurantType.CHINESE);
        addLabel(layer, 35.231564, 129.085253, "포포포", "짜조가 맛있는 쌀국수집", "https://maps.app.goo.gl/K5hTE7e45zCKp3EeA", RestaurantType.VIETNAM);
        addLabel(layer, 35.2301375, 129.0857935, "카츠안", "쫄순이 야무진 카츠", "https://maps.app.goo.gl/sUpHcxPUnNwxgZiz7", RestaurantType.SUSHI);
        addLabel(layer, 35.2310302, 129.084811, "야마벤또", "맛있는 튀김과 벤또", "https://g.co/kgs/3KSzyj4", RestaurantType.SUSHI);
        addLabel(layer, 35.23346110000001, 129.0803809, "금정회관", "학식", "https://lei.pusan.ac.kr/lei/55296/subview.do", RestaurantType.KOREAN);
        addLabel(layer, 35.2320819, 129.0854509, "교토밥상", "규카츠 맛집", "https://maps.app.goo.gl/eo4iD5sQGeZtktD4A", RestaurantType.SUSHI);
    }

    private void addLabel(LabelLayer layer, double latitude, double longitude, String restaurantName, String additionalInfo, String url, RestaurantType type) {
        int drawableId;
        switch (type) {
            case PIZZA:
                drawableId = R.drawable.pizza;
                break;
            case SUSHI:
                drawableId = R.drawable.sushi;
                break;

            case KOREAN:
                drawableId = R.drawable.korean;
                break;
            case VIETNAM:
                drawableId = R.drawable.vietnam;
                break;
            case CHINESE:
                drawableId = R.drawable.chinese;
                break;
            default:
                drawableId = R.drawable.location; // 기본 아이콘
                break;
        }

        LabelStyles labelStyles = LabelStyles.from(LabelStyle.from(drawableId));
        LabelOptions labelOptions = LabelOptions.from(LatLng.from(latitude, longitude)).setStyles(labelStyles);
        Label label = layer.addLabel(labelOptions);
        labelList.add(label); // 레이블 리스트에 추가
        labelToRestaurantMap.put(label, new RestaurantInfo(restaurantName, additionalInfo, url)); // 레이블과 식당 정보를 매핑
    }

    private void selectRandomLabel() {
        if (labelList.isEmpty()) {
            Toast.makeText(this, "레이블이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 랜덤으로 레이블 선택
        Random random = new Random();
        int index = random.nextInt(labelList.size());
        Label selectedLabel = labelList.get(index);

        // 선택된 레이블에 대한 식당 정보 가져오기
        RestaurantInfo restaurantInfo = labelToRestaurantMap.get(selectedLabel);
        LatLng position = selectedLabel.getPosition();

        // 선택된 레이블의 좌표로 카메라 이동 및 확대
        CameraUpdate moveToLabel = CameraUpdateFactory.newCenterPosition(position);
        kakaoMap.moveCamera(moveToLabel);

        CameraUpdate zoomIn = CameraUpdateFactory.zoomTo(19); // 확대 레벨 설정
        kakaoMap.moveCamera(zoomIn);

        // URL로 이동
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantInfo.getUrl()));
        startActivity(browserIntent);

        // 메시지 표시
        String message = "오늘 메뉴는 " + restaurantInfo.getName() + " 어때요?\n" + restaurantInfo.getAdditionalInfo();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private static class RestaurantInfo {
        private final String name;
        private final String additionalInfo;
        private final String url;

        public RestaurantInfo(String name, String additionalInfo, String url) {
            this.name = name;
            this.additionalInfo = additionalInfo;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public String getAdditionalInfo() {
            return additionalInfo;
        }

        public String getUrl() {
            return url;
        }
    }
}
