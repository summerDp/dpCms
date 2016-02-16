



app.directive(
		'simpleTable',
		function() {
			return {
				restrict : 'E',
				scope : {
					dataUrl :'@',
				},
				replace:true,
				templateUrl : basePath + '/js/function/common/simpleTable.html',
				link : function(scope, element, attrs) {
				},
				controller : function($scope,NgTableParams){
					
				}
			}
});
						
app.directive(
		'col',
		function() {
				return {
						restrict : 'E',
						scope : {
							title :'@',
						},
						replace:true,
						tempate :　function(tElement, tAttrs){
							console.info('colDirective tElement',tElement);
							console.info('colDirective tAttrs',tAttrs);
							return '这里是col输出的代码';
						},
						link : function(scope, element, attrs) {
						}
				}
		}
);