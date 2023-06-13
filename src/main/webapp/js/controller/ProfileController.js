function ProfileController($scope, $rootScope, UserService) {
	try {
	UserService.securityCheck($.param({
		secu : $rootScope.accessKey
	}), function(response) {
		console.log(response);
		if (!(response != null && response.result == "1")) {
			window.location.assign(getDomain() + "#/login");
		}
	});
	
	UserService.userProfile($.param({
		secu : $rootScope.accessKey
	}), function(response) {
		try{			
			$scope.profile=JSON.parse(response.json)
		}catch{
			console.log(response);
		}

	});
	
	$(function() {
		var Accordion = function(el, multiple) {
			this.el = el || {};
			this.multiple = multiple || false;

			// Variables privadas
			var links = this.el.find('.link');
			// Evento
			links.on('click', {
				el : this.el,
				multiple : this.multiple
			}, this.dropdown)
		}

		Accordion.prototype.dropdown = function(e) {
			var $el = e.data.el;
			$this = $(this);
			$next = $this.next();

			$next.slideToggle();
			$this.parent().toggleClass('open');

			if (!e.data.multiple) {
				$el.find('.submenu').not($next).slideUp().parent().removeClass(
						'open');
			}
			;
		}

		var accordion = new Accordion($('#accordion'), false);
	});
	} catch (e) {
		console.log(e);
	}

}