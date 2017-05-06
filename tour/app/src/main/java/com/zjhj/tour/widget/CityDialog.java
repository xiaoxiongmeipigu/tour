package com.zjhj.tour.widget;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zjhj.commom.result.CityModel;
import com.zjhj.commom.result.DistrictModel;
import com.zjhj.commom.result.ProvinceModel;
import com.zjhj.tour.R;
import com.zjhj.tour.widget.picker.ArrayWheelAdapter;
import com.zjhj.tour.widget.picker.ArrayWheelView;
import com.zjhj.tour.widget.picker.OnWheelChangedListener;
import com.zjhj.tour.widget.picker.WheelView;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 自定义省市区控件
 * 
 * @author brain
 * @since 2015.7.22
 */
public class CityDialog extends Dialog implements
View.OnClickListener, WheelView.OnScrollingStop,OnWheelChangedListener {

	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>(); 

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName="";
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName="";
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName ="";

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode ="";

	/**
	 * 解析省市区的XML数据
	 */

	private int cityPosition = 0;
	private int districtPosition = 0;
	private int provincePosition = 0;
	private boolean isCityFirst = true;
	private boolean isProvinceFirst = true;

	Gson gson = new Gson();

	List<ProvinceModel> provinceList = null;

	protected void initProvinceDatas()
	{


		AssetManager asset = context.getAssets();
		Scanner scanner = null;
		try {
			JSONObject object = JSONObject.parseObject(json);
			// 获取解析出来的数据

			/*provinceList  = JSON.parseObject(object.getJSONArray("data").getJSONObject(0).getJSONArray("_child").toJSONString(),
                                new TypeReference<List<ProvinceModel>>() {
                                });*/

			provinceList = gson.fromJson(object.getJSONArray("data").toJSONString(), new TypeToken<List<ProvinceModel>>(){}.getType());

//			provinceList = JSONArray.parseArray(object.getJSONArray("data").getJSONObject(0).getJSONArray("_child").toJSONString(), ProvinceModel.class);

			//*/ 初始化默认选中的省、市、区
			if (null != mCurrentProviceName && !"".equals(mCurrentProviceName)) {
				Log.i("info", "开始设置默认省市区：" + mCurrentProviceName + " " + mCurrentCityName + " " + mCurrentDistrictName);
				if (null != provinceList) {
					for (int i = 0; i < provinceList.size(); i++) {
						if (mCurrentProviceName.equals(provinceList.get(i).getRegion_name())) {
							Log.i("info", "设置省==>" + i);
							//							mViewProvince.setCurrentItem(i);
							provincePosition = i;
							for (int j = 0; j < provinceList.get(i).getCity_list().size(); j++) {
								if (mCurrentCityName.equals(provinceList.get(i).getCity_list().get(j).getRegion_name())) {
									Log.i("info", "设置市==>" + j);
									//									mViewCity.setCurrentItem(j);
									cityPosition = j;
									for (int k = 0; k < provinceList.get(i).getCity_list().get(j).getArea_list().size(); k++) {
										if (mCurrentDistrictName.equals(provinceList.get(i).getCity_list().get(j).getArea_list().get(k).getRegion_name())) {
											Log.i("info", "设置区===>" + k);
											//											mViewDistrict.setCurrentItem(k);
											districtPosition = k;
											break;
										}
									}
									break;
								}
							}
							break;
						}
					}

				}


			} else {
				if (provinceList != null && !provinceList.isEmpty()) {
					mCurrentProviceName = provinceList.get(0).getRegion_name();
					List<CityModel> cityList = provinceList.get(0).getCity_list();
					if (cityList != null && !cityList.isEmpty()) {
						mCurrentCityName = cityList.get(0).getRegion_name();
						List<DistrictModel> districtList = cityList.get(0).getArea_list();
						if (districtList != null && !districtList.isEmpty()) {
							mCurrentDistrictName = districtList.get(0).getRegion_name();
							mCurrentZipCode = districtList.get(0).getRegion_id();
						}

					}
				}
			}
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getRegion_name();
				List<CityModel> cityList = provinceList.get(i).getCity_list();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getRegion_name();
					List<DistrictModel> districtList = cityList.get(j).getArea_list();
					String[] distrinctNameArray = new String[districtList.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(districtList.get(k).getRegion_name(), districtList.get(k).getRegion_id());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getRegion_name(), districtList.get(k).getRegion_id());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getRegion_name();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getRegion_name(), cityNames);
			}
			stopProgressDialog();

		}catch (Throwable e) {
			e.printStackTrace();  
		}finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}



	public interface PriorityListener {
		public void refreshPriorityUI(String proviceName, String cityName, String districtName);
		public void refreshPriorityUI(String proviceName, String cityName, String districtName, String proviceId, String cityId, String districtId);
	}

	private PriorityListener lis;
	private boolean scrolling = false;
	private Context context = null;

	private ArrayWheelView mViewProvince;
	private ArrayWheelView mViewCity;
	private ArrayWheelView mViewDistrict;

	private static int theme = R.style.MyDialogStyle;// 主题
	private LinearLayout date_layout;
	private int width, height;// 对话框宽高
	private TextView title_tv;
	private String title;
	private LinearLayout confirmLL;
	LinearLayout confirm_layouttwo;
	LinearLayout tapLinearLayout;
	LinearLayout comLinearLayout;
	/**
	 * 圆形进度
	 */
	private CustomProgressDialog progressDialog = null;
	private String json;
	public CityDialog(Context context,String json, PriorityListener listener) {
		super(context, theme);
		this.context = context;
		this.json = json;
	}

	public CityDialog(Context context) {
		super(context, theme);
		this.context = context;
	}

	public CityDialog(final Context context,String json, final PriorityListener listener,
			int width,int height, String title,String mCurrentProviceName,String mCurrentCityName,String mCurrentDistrictName) {
		super(context, theme);
		this.context = context;
		this.json = json;
		lis = listener;
		this.width = width;
		this.title = title;
		this.height = height;
		this.mCurrentProviceName = mCurrentProviceName;
		this.mCurrentCityName = mCurrentCityName;
		this.mCurrentDistrictName = mCurrentDistrictName;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_city_select_wheel);
		setUpViews();
		setUpListener();
		isCityFirst = true;
		isProvinceFirst = true;
		setUpData();


	}

	private void setUpViews() {

		date_layout = (LinearLayout) findViewById(R.id.date_selelct_layout);
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		LayoutParams lparams_hours = new LayoutParams((int)(300*dm.density), (int)(170*dm.density));
		date_layout.setLayoutParams(lparams_hours);
		title_tv = (TextView) findViewById(R.id.diaolog_title_tv);
		title_tv.setText(title);

		confirmLL = (LinearLayout)findViewById(R.id.confirm_layout);
		confirm_layouttwo = (LinearLayout)findViewById(R.id.confirm_layouttwo);
		tapLinearLayout = (LinearLayout)findViewById(R.id.tap_layout);
		comLinearLayout = (LinearLayout)findViewById(R.id.com_layout);

		mViewProvince = (ArrayWheelView) findViewById(R.id.id_province);
		mViewCity = (ArrayWheelView) findViewById(R.id.id_city);
		mViewDistrict = (ArrayWheelView) findViewById(R.id.id_district);
	}

	public void setBomTwoView(){
		confirm_layouttwo.setVisibility(View.VISIBLE);
		confirmLL.setVisibility(View.GONE);
	}

	private void setUpListener() {
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
		// 添加onclick事件
		confirmLL.setOnClickListener(this);
		tapLinearLayout.setOnClickListener(this);
		comLinearLayout.setOnClickListener(this);
	}

	private void setUpData() {
		startProgressDialog("");
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, mProvinceDatas));
		mViewProvince.setCurrentItem(provincePosition);
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(ArrayWheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		try{


			if(wheel == mViewProvince){
				if(isProvinceFirst){
					Log.i("info","onChanged==isProvinceFirst");
					isProvinceFirst = false;
				}else{
					cityPosition = 0;
				}
			}

			if (wheel == mViewProvince) {
				updateCities();
			} else if (wheel == mViewCity) {
				if(isCityFirst){
					Log.i("info","onChanged==isCityFirst");
					isCityFirst = false;
				}else{
					districtPosition = 0;
				}
				updateAreas();
			} else if (wheel == mViewDistrict) {
				if(mDistrictDatasMap.get(mCurrentCityName)!=null&&mDistrictDatasMap.get(mCurrentCityName).length!=0){
					mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
					mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		try{
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		Log.i("info","==mCurrentCityName="+mCurrentCityName);
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);
		Log.i("info","======areas======"+(areas.length));
		if (areas == null||areas.length==0) {
			mCurrentDistrictName = "";
			areas = new String[] { "" };
		}
		else{

			mCurrentDistrictName = areas[districtPosition];//0
		}
		Log.i("info","mCurrentDistrictName=>"+mCurrentDistrictName);
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
		mViewDistrict.setCurrentItem(districtPosition);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null||cities.length==0) {
			mCurrentCityName = "";
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
		mViewCity.setCurrentItem(cityPosition);
		updateAreas();
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm_layout:
			Log.i("info","confirm_layout");
			lis.refreshPriorityUI(mCurrentProviceName,
					mCurrentCityName, mCurrentDistrictName,provinceList.get(mViewProvince.getCurrentItem()).getRegion_id(),
					provinceList.get(mViewProvince.getCurrentItem()).getCity_list().get(mViewCity.getCurrentItem()).getRegion_id(),
					provinceList.get(mViewProvince.getCurrentItem()).getCity_list().get(mViewCity.getCurrentItem()).getArea_list().get(mViewDistrict.getCurrentItem()).getRegion_id());

			this.dismiss();
			break;
		case R.id.tap_layout:
			Log.i("info","tap_layout");
			lis.refreshPriorityUI("",
					"", "");

			this.dismiss();
			break;
		case R.id.com_layout:
			Log.i("info","com_layout");
			lis.refreshPriorityUI(mCurrentProviceName,
					mCurrentCityName, mCurrentDistrictName);

			this.dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onScrollingStop() {
		//		lis.refreshPriorityUI(year_adapter.getValues().substring(0, 4),
		//				month_adapter.getValues().substring(0, 2), day_adapter
		//						.getValues().substring(0, 2));
	}

	/**
	 * 显示进度
	 */
	public void startProgressDialog(String msg) {
		if (progressDialog == null) {
			progressDialog = new CustomProgressDialog(context, msg);
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 关闭进度
	 */
	public void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}


	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {

	}

}


