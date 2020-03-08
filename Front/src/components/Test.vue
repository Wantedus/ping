<template>
	<div id="box">
		<input type="button" name="getMessage" @click="$goto('/Recherche')" value="Go to Recherche"/>
		<input type="button" name="getMessage" @click="$goto('/Creation')" value="Go to Creation"/>
		<input type="button" name="getMessage" @click="$goto('/Modification')" value="Go to Modification"/>
		<!-- <p :disabled=!listData> {{ listData }}</p> -->
	</div>
</template>

<script type="text/javascript">
	export default {
		name: 'Test',
		data(){
			return{
				listData: null
				
			}
		},
		methods: {
			// get:function(){
			// 	this.axios.get('http://localhost:8080/message').then((response)=>{
			//         this.listData=response.data;
			//       }).catch((response)=>{
			//         console.log(response);
			//       })
			// }			
		}
	}
</script>