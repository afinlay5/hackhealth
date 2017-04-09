var app = angular.module("hackHealth", []);

app.controller("hACKHealthCtrl", [ "$scope", "$interval", "$filter", "$timeout", "$http", ($scope, $interval, $filter, $timeout, $http) => {
	//application variables
	$scope.name1 = "HA";
	$scope.name2 = "KHealth";
	$scope.description = "what's the perfect meal for you?";
	$scope.mealChoice = "enter a meal";
	$scope.placeHolder = "name";
	$scope.nav = 0;
	$scope.answer;
	$scope.user;
	$scope.height;
	$scope.weight;
	$scope.gender;
	$scope.age;
	$scope.coeff_1 = 10;
	$scope.coeff_2 = 6.25;
	$scope.coeff_3 = 4.92;
	$scope.coeff_M = 5;
	$scope.coeff_F = 161;
	$scope.cm = 2.54;
	$scope.kilograms = 2.20462;
	$scope.weight_kg;
	$scope.height_cm;
	$scope.result;
	$scope.activity;
	$scope.appId = "a9079da7";
	$scope.appKey = "0fb273b930f38535b66951c9331ad26d";
	$scope.itemName1;
	$scope.itemCalories1;
	$scope.itemProtein1;
	$scope.itemSodium1;
	$scope.itemSugars1;
	$scope.itemFat1;
	$scope.itemName2;
	$scope.itemCalories2;
	$scope.itemProtein2;
	$scope.itemSodium2;
	$scope.itemSugars2;
	$scope.itemFat2;
	$scope.itemName3;
	$scope.itemCalories3;
	$scope.itemProtein3;
	$scope.itemSodium3;
	$scope.itemSugars3;
	$scope.itemFat3;
	
	//calculate the BMR after all information is found
	$scope.calc = () => {
		//calculation for male and female
		if ($scope.gender == "male") {
			//male value
			$scope.result = ($scope.coeff_1 * $scope.weight_kg) + ($scope.coeff_2 * $scope.height_cm) - ($scope.coeff_3 * $scope.age) + $scope.coeff_M;
			if($scope.result < 1000){
				$scope.result = $scope.result * $scope.activity;
			}
		} else if ($scope.gender == "female") {
			//female value
			$scope.result = $scope.coeff_1 * $scope.weight_kg + $scope.coeff_2 * $scope.height_cm - $scope.coeff_3 * $scope.age - $scope.coeff_F;
			if($scope.result < 1000){
				$scope.result = $scope.result * $scope.activity;
			}
		}

		//bring in conclusion page
		$("#mainPage").fadeOut(800);
		$timeout( () => {
			$("#resultsPage").fadeIn(800);
		}, 900);
	}
	

	//move to the next input value
	$scope.next = () => {
		//cache the input value into respective variable
		$scope.answer = $("#userInput").val();
		//move to the next user input
		$scope.nav++;
		//clear the input field and focus for the next user input
		$("#userInput").val("").focus();
	}

	//move to the previous input
	$scope.prev = () => {
		$scope.nav--;
		$("#userInput").val("").focus();

		//keep the value positive
		if($scope.nav < 0){
			$scope.nav = 0;
		}
	}

	$scope.updateInput = () =>{
		if($scope.nav == 0){
			$scope.placeHolder = "name";
		} else if($scope.nav == 1){
			$scope.placeHolder = "height";
			$scope.user = $scope.answer;
		} else if($scope.nav == 2){
			$scope.placeHolder = "weight";
			$scope.height = $scope.answer;
		} else if($scope.nav == 3){
			$scope.placeHolder = "gender";
			$scope.weight = $scope.answer;
		} else if($scope.nav == 4){
			$scope.placeHolder = "age";
			$scope.gender = $scope.answer;
		} else if ($scope.nav == 5){
			$scope.placeHolder = "";
			$scope.age = $scope.answer;
			$("#userInput").fadeOut(600);
			$("#navigation").fadeOut(600);
			$timeout( () => {
				$("#activeLevel").css("display", "flex");
			}, 600);
		} else if ($scope.nav == 6) {
			$scope.activity = $(".selected").attr("data");
			$("#activeLevel").fadeOut(600);
			$timeout( () => {
				$("#userInput").fadeIn(600);
				$("#navigation").fadeIn(600);
			}, 600);

			$scope.placeHolder = "submit?";
			$scope.weight_kg = $scope.weight / $scope.kilograms; 
			$scope.height_cm = $scope.height * 12 * $scope.cm;
		} else if ($scope.nav == 7) {
			console.log($scope.activity);
			$scope.calc();
		}
	}

	$scope.activeSelect = () => {
		$(this).addClass("selected");
	}
	//when the enter buttom is pressed go to next user input
	$("#userInput").keypress( (e) => {
		if(e.which == 13){
			$scope.next();
			$timeout( () => {
				$scope.updateInput();
			}, 10);
		}
	});

	//when the enter buttom is pressed display the three meals
	$("#foodInputBox").keypress( (e) => {
		if(e.which == 13){
			//cache the input value to the search link
			$scope.foodChoice = $("#foodInputBox").val();
			$scope.search();
		}
	});

	$scope.search = () => {
		$http({
		  method: 'GET',
		  url: "https://api.nutritionix.com/v1_1/search/" + $scope.foodChoice + "?fields=item_name%2Cnf_calories%2Cnf_total_fat%2Cnf_sodium%2Cnf_sugars%2Cnf_protein%2Cnf_calories&appId=" + $scope.appId + "&appKey=" + $scope.appKey
		}).then(function successCallback(response) {
			//get BMR for one meal assuming four meals a day
			var mealsPerDay = 4;
			var oneMeal = $scope.result / mealsPerDay;
			console.log(oneMeal);
			//cache the responses
			var hits = response.data.hits;
			//get length of response
			var l = hits.length;
			//number of response needed
			var maxResponses = 0;
			//loop through responce
			for( var i = 0; i < l; i ++){
				//cache number of calories
				var cal = hits[i].fields.nf_calories;
				if(oneMeal >= cal){
					if(maxResponses == 0){
						$scope.itemName1 = hits[i].fields.item_name;
						$scope.itemCalories1 = hits[i].fields.nf_calories;
						$scope.itemProtein1 = hits[i].fields.nf_protein;
						$scope.itemSodium1 = hits[i].fields.nf_sodium;
						$scope.itemSugars1 = hits[i].fields.nf_sugars;
						$scope.itemFat1 = hits[i].fields.nf_total_fat;
						maxResponses++;
					} else if(maxResponses == 1){
						$scope.itemName2 = hits[i].fields.item_name;
						$scope.itemCalories2 = hits[i].fields.nf_calories;
						$scope.itemProtein2 = hits[i].fields.nf_protein;
						$scope.itemSodium2 = hits[i].fields.nf_sodium;
						$scope.itemSugars2 = hits[i].fields.nf_sugars;
						$scope.itemFat2 = hits[i].fields.nf_total_fat;
						maxResponses++;
					} else if(maxResponses == 2){
						$scope.itemName3 = hits[i].fields.item_name;
						$scope.itemCalories3 = hits[i].fields.nf_calories;
						$scope.itemProtein3 = hits[i].fields.nf_protein;
						$scope.itemSodium3 = hits[i].fields.nf_sodium;
						$scope.itemSugars3 = hits[i].fields.nf_sugars;
						$scope.itemFat3 = hits[i].fields.nf_total_fat;
						maxResponses++;
					}
					
					if(maxResponses == 3){
						break;
					}
				} else {
					console.log("no meals found");
				}
			}
		  }, function errorCallback(response) {
		    console.log(response.statusText);
		  });
	};

}]);