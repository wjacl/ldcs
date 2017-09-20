var ldcsComm = {
	minuteFormat:function(value,row,index){
		var str = "";
		if(value != undefined){
			if(value){
				var h = Math.floor(value / 60);
				if(h > 0){
					str += h + "小时";
				}
				var m = value % 60;
				if(m > 0){
					str += m + "分";
				}
			}
		}
		return str;
	}
};