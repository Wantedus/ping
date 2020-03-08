<template>
    <div id="tuto">
        <header>
            <nav class="navbar navbar-expand-sm bg-light">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <button type="menu" class="btn btn-secondary btn-lg btn-block" data-toggle="collapse" data-target="#demo"> Menu </button>
                        <div id="demo" class="collapse">
                            <br>
                            <a class="p-2 text-muted" href="#"> Authentification</a>
                            <br><br>
                            <p class="p-2 text-muted" @click="$goto('/Modification')">Création d'un Message</p>
                            
                        </div>
                    </li>

                    <li class="nav-item mx-auto">
                        <a class="nav-link disable "  href="#">Bienvenue madame/monsieur xxxx</a>
                    </li>
                </ul>
            </nav>
            <br>

            <form  class="row mx-md-n5">
                <div class="col px-md-5">
                <p>Rechercher par : </p>
                <div class="radio">
                    <label>
                        <input type="radio" name="optradio" value="libelle" v-model="optradio">Libellé</label>
                    <label>
                        <input type="radio" name="optradio" value="motcle" v-model="optradio"> Mots clés</label>
                </div>
                </div>

                <br>
                <div class="col px-md-5">
                <p>Type de message :</p>
                <div class="radio">
                    <label>
                        <input type="radio" name="type" value="information" v-model="type">Informations</label>
                    <label>
                        <input type="radio" name="type" value="bulle" v-model="type"> Bulle</label>
                </div>
                </div>
            </form>
        </header>
        <br>

        <div>
            <form class="form-inline" >
                <input class="form-control mr-sm-2 col-sm-8" type="text" placeholder="Search" v-model="recherche">
               <!-- <button class="btn btn-secondary" type="submit" >Rechercher</button> -->
            </form>
            <br>
            <div class="panel panel-primary liste" v-show="messages.length">
                <table class="table table-bordered table-striped">
                    <thead>
                        <tr>
                        <th class="col-xs-1">Etat</th>
                        <th class="col-xs-2">Libellé</th>
                        <th class="col-xs-2">Date de début</th>
                        <th class="col-xs-2">Date de fin</th>
                        <th class="col-xs-2">Fédération</th>
                        <th class="col-xs-3"></th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr v-for="(message, index) in displayedMessages" :key="index">
                        <!-- <td><span class="badge">.{{ badgeColor }}</span></td> -->
                        {{ changeColor(message) }}
                        
                        <td><span :class=badgeColor>.</span></td>
                        <td>{{ message.textLib }}</td> 
                        <td>{{ message.start }}</td> 
                        <td>{{ message.end }}</td> 
                        <td>{{ message.t.federation }}</td> 
                        <td><button class="btn btn-warning btn-block" @click="$goto('/Modification')"> M </button></td>
                        <td><button class="btn btn-success btn-block" @click="$goto('/Duplication')"> D </button></td>
                        <td><button class="btn btn-danger btn-block" @click="$goto('/Suppression')"> S </button></td>      
                        </tr>  
                    </tbody>       
                </table>
                          
                <!--li v-for="message in messages | filterBy 'recherche'in 'optradio' "></li-->
            </div> 
            <br>
        </div>
            <nav aria-label="Page navigation example">
            <ul class="pagination">
                <li class="page-item">
                    <button type="button" class="page-link" v-if="page != 1" @click="page--"> Previous </button>
                </li>
                
                <li class="page-item">
                    <button type="button" @click="page++" v-if="page < pages.length" class="page-link"> Next </button>
                </li>
            </ul>
        </nav>  

        <!--nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
              <li class="">
                <span><a class="" href="#" >1-10</a></span>
                <span>/</span>
                <span><a class="" href="#" >30 ></a></span>
              </li>
            </ul>
        </nav--> 
    </div>
</template>

<script>
export default {
    data() {
        return {
             optradio:'libelle',
            type:'information',
            recherche:'',
            badge: 'badge badge-pill badge-',
            messages : [],
            color: '',
            page: 1,
            perPage: 3,
            pages: [],  
            
        }
    },
        methods:{
        setPages () {
            let numberOfPages = Math.ceil(this.messages.length / this.perPage);
            for (let index = 1; index <= numberOfPages; index++) {
                this.pages.push(index);
            }
        },
        paginate (posts) {
            let page = this.page;
            let perPage = this.perPage;
            let from = (page * perPage) - perPage;
            let to = (page * perPage);
            return  posts.slice(from, to);
        },
            
            getTable:function(){
                 
                // const url = 'http://localhost:8092/message.json';
                // axios.get(url)
                //     .then(data=>console.log(data))
                //     .catch(err=>console.log(err))

                // Recuperer et remplir la variable message à l'aide d'une requete http
                /*this.messages = [
                    {etat: "en cours", libelle:"x1111", dateDebut:"09/10/19", dateFin:"10/10/19", federation:"ABP"},
                    {etat: "en attente", libelle:"x1211", dateDebut:"06/08/19", dateFin:"06/08/19", federation:"WEB"},
                
                {etat: "terminé", libelle:"x1311", dateDebut:"02/02/19", dateFin:"04/02/19", federation:"CMB"},
                    {etat: "en cours", libelle:"x1411", dateDebut:"10/01/19", dateFin:"11/10/19", federation:"CCMC"},
                    {etat: "en cours", libelle:"test", dateDebut:"test", dateFin:"test", federation:"test"},
                    {etat: "en attente", libelle:"x1611", dateDebut:"25/11/19", dateFin:"26/11/19", federation:"CMB"},
                    {etat: "terminé", libelle:"x1511", dateDebut:"14/03/19", dateFin:"15/03/19", federation:"WEB"},
                ]*/

                    
                        axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
                        axios
                        .get('http://localhost:8090/search/'+this.optradio+'?'+this.optradio+'='+this.recherche+'&type='+this.type+'&page='+this.page)
                        .then(response =>{
                             (this.messages = response.data.data);

                            // Attribut l'état du badge en fonction de l'état du message
                            for(let i = 0; i < this.messages.length; i++){
                                let currentDate = Date.now();
                                let end = Date.parse(this.messages[i].end);
                                let start = Date.parse(this.messages[i].start)

                                if( !end || (start < currentDate && currentDate < end)){
                                    this.messages[i].etat == "en cours"
                                    this.messages[i].badge = "warning" // Jaune

                                }else if(currentDate < start){
                                    this.messages[i].etat == "en attente"
                                    this.messages[i].badge = "danger" // Rouge

                                }else if(currentDate > end ){
                                    this.messages[i].etat == "terminé"
                                    this.messages[i].badge = "success" // Vert
                                }
                            }
                        }
                        
                        )
        
            },
            changeColor(message){
                this.color=message.badge
            }
            
    },created:function(){
        this.getTable();
    },computed: {
        badgeColor(){
            return this.badge + this.color
        },
                displayedMessages () {
            return this.paginate(this.messages);
        }
    },
    watch: {
        messages () {
            this.setPages();
        }, recherche (){
            if(this.recherche.length > 3)
                this.getTable();
        },optradio(){
            this.getTable();
        },type(){
            this.getTable();
        }
    },filters: {
        trimWords(value){
            return value.split(" ").splice(0,20).join(" ") + '...';
        }
    }

}
</script>

<style>
    .liste{
    max-height: 50vh;
    overflow-y: auto;
    overflow-x: hidden;
}

.badge-pill{
    color: #0000; /* Pour rendre l'écrit au sein du badge invisible */
    margin-left: 1vh;
}
</style>

