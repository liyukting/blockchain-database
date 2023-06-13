function getDomain(){
	var url = window.location.href.split('#');
	if(url.length > 1 && url[0].trim() != ''){
		return url[0];
	}
	return '';
}
