package com.pnuppp.ppp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

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

    public enum RestaurantType {
        PIZZA, SUSHI, KOREAN, CHINESE, VIETNAM
    }

    MapView mapView;
    KakaoMap kakaoMap;
    LabelManager labelManager;
    List<Label> labelList = new ArrayList<>();
    Map<Label, RestaurantInfo> labelToRestaurantMap = new HashMap<>();
    Button selectLabelButton;
    TextView restaurantInfoTextView;
    TextView restaurantInfo2TextView;
    CheckBox openLinkCheckbox; // CheckBox 추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newnew);

        mapView = findViewById(R.id.map_view);
        selectLabelButton = findViewById(R.id.select_label_button);
        restaurantInfoTextView = findViewById(R.id.restaurant_info_text_view);
        restaurantInfo2TextView = findViewById(R.id.restaurant_info2_text_view);
        openLinkCheckbox = findViewById(R.id.open_link_checkbox); // CheckBox 초기화

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

        addLabel(layer, 35.2304389, 129.0842777, "톤쇼우", "부산대 근본 카츠임", "https://g.co/kgs/F6ZsNBX", RestaurantType.SUSHI);
        addLabel(layer, 35.2301701, 129.085188, "마마도마", "밥약 국룰 스시", "https://g.co/kgs/eHQ3FQL", RestaurantType.VIETNAM);
        addLabel(layer, 35.230478, 129.0866165, "버거킹", "쿠폰 생기면 버거킹 앱 들고 뛰어가셈", "https://g.co/kgs/DUL2e2F", RestaurantType.PIZZA);
        addLabel(layer, 35.230273, 129.0851434, "미분당", "조용한 쌀국수", "https://g.co/kgs/MyeJqTd", RestaurantType.SUSHI);
        addLabel(layer, 35.228884, 129.0880355, "웍헤이", "보기엔 평범하지만 맛있음", "https://g.co/kgs/QcmgSLD", RestaurantType.CHINESE);
        addLabel(layer, 35.231564, 129.085253, "포포포", "짜조가 맛있는 쌀국수집", "https://maps.app.goo.gl/K5hTE7e45zCKp3EeA", RestaurantType.VIETNAM);
        addLabel(layer, 35.2301375, 129.0857935, "카츠안", "쫄순이 야무진 카츠, 무려 천원짜리임", "https://maps.app.goo.gl/sUpHcxPUnNwxgZiz7", RestaurantType.SUSHI);
        addLabel(layer, 35.2310302, 129.084811, "야마벤또", "맛있는 튀김과 벤또", "https://g.co/kgs/3KSzyj4", RestaurantType.SUSHI);
        addLabel(layer, 35.23346110000001, 129.0803809, "금정회관", "학식", "https://lei.pusan.ac.kr/lei/55296/subview.do", RestaurantType.KOREAN);
        addLabel(layer, 35.2320819, 129.0854509, "교토밥상", "규카츠 맛집", "https://maps.app.goo.gl/eo4iD5sQGeZtktD4A", RestaurantType.SUSHI);
        addLabel(layer, 35.2326694, 129.0850967, "나오리쇼쿠", "오차즈케와 연어덮밥", "https://g.co/kgs/tpWGKxK", RestaurantType.SUSHI);
        addLabel(layer, 35.231994, 129.0874216, "코하루", "라면 맛있음 야무짐", "https://g.co/kgs/DSduFmt", RestaurantType.SUSHI);
        addLabel(layer, 35.2319444, 129.0872222, "장호가양꼬치", "마파두부도 맛있음 근데 양꼬치도 ㄹㅈㄷ임", "https://m.blog.naver.com/00010714/222841135435", RestaurantType.CHINESE);
        addLabel(layer, 35.2295171, 129.0891292, "프랭크버거", "미국식 버거, 맛있음 양파기름 맛있음", "https://blog.naver.com/ann5578/222705611370?viewType=pc", RestaurantType.PIZZA);
        addLabel(layer, 35.2282393, 129.0888936, "도야지면옥", "수육이 ㄹㅈㄷ임 같이시키셈", "https://g.co/kgs/8u2QW7B", RestaurantType.KOREAN);
        addLabel(layer, 35.22963807159382, 129.08381144581645, "펠로피자", "치즈하면 이재모, 빵하면 펠로피자", "https://g.co/kgs/vmwxFbu", RestaurantType.PIZZA);
        addLabel(layer, 35.2306434, 129.085078, "곁집", "육회비빔밥+된장 조합 추천", "https://g.co/kgs/8dhmv1W", RestaurantType.KOREAN);
        addLabel(layer, 35.23112202121665, 129.08564060926437, "고메밀면", "육전물밀면 맛있음 시원함", "https://g.co/kgs/vqS2FeG", RestaurantType.KOREAN);
        addLabel(layer, 35.2303216, 129.0858729, "우쭈쭈", "쌈무+깻잎에 쭈꾸미 싸먹으면 맛있음", "https://g.co/kgs/bx4J9J4", RestaurantType.KOREAN);
        addLabel(layer, 35.2299123, 129.0859038, "유가네닭갈비", "뭐 먹을지 고민될때? ㄱㄱ", "https://g.co/kgs/PdQbqbc", RestaurantType.KOREAN);

        addLabel(layer, 35.2307740230308, 129.084148433224, "따신밥", "알밥과 비빔밥", "https://g.co/kgs/VXiTbgY", RestaurantType.KOREAN);
        addLabel(layer, 35.2313981574196, 129.084389112168, "명품토스트", "부산대 정문의 유명한 전통 토스트", "https://g.co/kgs/3wcpTNw", RestaurantType.KOREAN);
        addLabel(layer, 35.2388835254308, 129.083783040998, "뉴숯불치킨", "맛있는 부산대 옛날치킨", "https://g.co/kgs/qMjRhW3", RestaurantType.KOREAN);
        addLabel(layer, 35.2307266150671, 129.084405206142, "유림부대밀면", "깔끔한 밀면", "https://g.co/kgs/me8cv7M", RestaurantType.KOREAN);
        addLabel(layer, 35.2305794711296, 129.084754866645, "평화김해뒷고기", "저기압일땐 고기앞으로 가라", "https://blog.naver.com/sallysophia/223516268662", RestaurantType.KOREAN);
        addLabel(layer, 35.230124069523, 129.086153831259, "등촌샤브칼국수", "국물이 맛있는 샤브샤브", "https://g.co/kgs/2C45FnS", RestaurantType.KOREAN);
        addLabel(layer, 35.2284331858253, 129.087818745151, "포맨티코", "감성있는 베트남 음식", "https://g.co/kgs/KuhMMnt", RestaurantType.VIETNAM);
        addLabel(layer, 35.233504813509, 129.08585073523, "수림식당", "탄탄면, 가지덮밥 중식당", "https://maps.app.goo.gl/oQ6DA9fBnkqLsh5V9", RestaurantType.CHINESE);
        addLabel(layer, 35.2291055299581, 129.085446498492, "칠성돌곱창", "곱창, 막창", "https://maps.app.goo.gl/LQBo4QZ9ykz3gy9m9", RestaurantType.KOREAN);
        addLabel(layer, 35.2303479915382, 129.085542994759, "자마미등갈비", "오늘은 등갈비?", "https://maps.app.goo.gl/iwWJiNLQPNM2eoGu5", RestaurantType.KOREAN);


        addLabel(layer, 35.2330282429425, 129.084838958525, "홍콩반점", "무난한 중국집", "https://maps.app.goo.gl/QPPtTPTzhAEqLKrX7", RestaurantType.CHINESE);
        addLabel(layer, 35.2370545224952, 129.082675549778, "두부마을", "북문의 순두부 맛집", "https://naver.me/GlVWS6Be", RestaurantType.KOREAN);
        addLabel(layer, 35.2368691697718, 129.083648242743, "만사성", "옛날 느낌의 맛있는 중국집", "https://g.co/kgs/yc22V63", RestaurantType.CHINESE);
        addLabel(layer, 35.2314681382058, 129.085845038089, "리틀프랑", "버섯이 맛있는 돈까스", "https://g.co/kgs/o39vUtq", RestaurantType.SUSHI);
        addLabel(layer, 35.2309176827367, 129.085982944922, "진주비봉식당", "국밥이 맛있다. 매우 맛있다.", "https://g.co/kgs/ySjX8Jz", RestaurantType.KOREAN);
        addLabel(layer, 35.2283320598859, 129.088059964159, "비비드드림", "파스타가 맛있다. 가지파스타도 있다. 맛있다.", "https://m.blog.naver.com/rkgus1891/222898663167", RestaurantType.PIZZA);
        addLabel(layer, 35.2325850983781, 129.084894173087, "칠칠켄터키", "치킨에 맥주 ㄱ?", "https://g.co/kgs/CPf91c5", RestaurantType.KOREAN);
        addLabel(layer, 35.2308634683995, 129.086639426884, "두끼", "떡볶이가 땡겨? 많이 먹고 싶어? ㄱ", "https://g.co/kgs/uQDR5ps", RestaurantType.KOREAN);








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
                drawableId = R.drawable.location;
                break;
        }

        // LabelOptions 객체를 생성하기 위해 LabelOptions.from() 메서드를 사용
        LabelStyles labelStyles = LabelStyles.from(LabelStyle.from(drawableId));
        LabelOptions options = LabelOptions.from(LatLng.from(latitude, longitude)).setStyles(labelStyles);

        // 레이블 추가
        Label label = layer.addLabel(options);
        labelList.add(label);
        labelToRestaurantMap.put(label, new RestaurantInfo(restaurantName, additionalInfo, url, type)); // 수정된 부분
    }

    private void selectRandomLabel() {
        if (labelList.isEmpty()) {
            restaurantInfoTextView.setText("레이블이 없습니다.");
            return;
        }

        Random random = new Random();
        int index = random.nextInt(labelList.size());
        Label selectedLabel = labelList.get(index);

        RestaurantInfo restaurantInfo = labelToRestaurantMap.get(selectedLabel);
        LatLng position = selectedLabel.getPosition();

        CameraUpdate moveToLabel = CameraUpdateFactory.newCenterPosition(position);
        kakaoMap.moveCamera(moveToLabel);

        CameraUpdate zoomIn = CameraUpdateFactory.zoomTo(19);
        kakaoMap.moveCamera(zoomIn);

        // CheckBox 상태에 따라 링크 열기
        if (openLinkCheckbox.isChecked()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantInfo.getUrl()));
            startActivity(browserIntent);
        }

        restaurantInfoTextView.setText(HtmlCompat.fromHtml(String.format(getString(R.string.random_restaurant_info), restaurantInfo.getName()), HtmlCompat.FROM_HTML_MODE_LEGACY));
        restaurantInfo2TextView.setText(restaurantInfo.getAdditionalInfo());
    }

    private class RestaurantInfo {
        private final String name;
        private final String additionalInfo;
        private final String url;
        private final RestaurantType type;

        public RestaurantInfo(String name, String additionalInfo, String url, RestaurantType type) {
            this.name = name;
            this.additionalInfo = additionalInfo;
            this.url = url;
            this.type = type;
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

        public RestaurantType getType() {
            return type;
        }
    }
}
