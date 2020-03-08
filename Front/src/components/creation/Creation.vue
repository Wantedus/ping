<template>
  <div id="app">
    <div class="menu">
        <nav id="sidebar">
        <h1>PING 31</h1>
         <hr>
        <ul class="list-unstyled components mb-5">
          <li class="active">
            <a @click="$goto('/Recherche')"><span class="fa fa-home mr-3"></span> Accueil</a>
             <hr>
          </li>
          <li>
              <a @click="$goto('/Creation')"><span class="fa fa-plus mr-3"></span>Creer Message</a>
               <hr>
          </li>
        </ul>

      </nav>
    </div>
    <div class="content">
      <div class="barre">
      <ul class="barre-ul">
        <li><a class="active" href="#home">Creation Message</a></li>
      </ul>
      </div>
      <div class="col-sm-10">
      <div class="titleprghp">
        <span class="prg">   </span>  
      </div>

      <form id="app" @submit="checkForm">
        <div class="form-group row">
          <label for="libelle" class="col-sm-2 col-form-label">Libelle:</label>
          <div class="col-sm-10">
            <input type="libelle" class="form-control" id="libelle" v-model="libelle">
          </div>
        </div>

        <fieldset class="form-group">
          <div class="row">
            <legend class="col-form-label col-sm-2 pt-0">Type de Message:</legend>
            <div class="col-sm-10">
              <div class="form-check-inline">
                <input class="form-check-input" type="radio"   name="gridRadios" id="gridRadios1" value="option1"  v-model="type">
                <label class="form-check-label" for="gridRadios1">
                Information
                </label>
  
              </div>
              <div class="form-check-inline">
                <input class="form-check-input" type="radio"   name="gridRadio2" id="gridRadios2" value="option2" v-model="type">
                <label class="form-check-label" for="gridRadios2">
                Bulle
                </label>
              </div>
              <div class="form-check">
                <input class="form-check-input" type="checkbox" value="" id="defaultCheck1">
                <label class="form-check-label" for="defaultCheck1">
                Restituer ce message sur la Vision 360
                </label>
              </div>
            </div>
          </div>
        </fieldset>
        <div class="form-group row">
          <div class="col-sm-2">Texte:</div>
            <div class="col-sm-10">
              <textarea class="form-control" id="txt" rows="3" v-model="textMessage"></textarea>
            </div>
          </div>

        <div class="form-group row">
          <label for="motcles" class="col-sm-2 col-form-label">Mot(s) Cle(s):</label>
          <div class="col-sm-10">
            <input type="motcles" class="form-control" id="motcles" v-model="motcle">
          </div>
        </div>

        <div class="form-group row">
          <label for="debut" class="col-sm-2 col-form-label">Debut d'Affichage:</label>
          <div class="col-sm-10">
            <input class="form-control" id="date" v-model="start" name="date" placeholder="MM/DD/YYY" type="date"/>
          </div>
        </div>

        <div class="form-group row">
          <label for="fin" class="col-sm-2 col-form-label">Fin d'Affichage:</label>
          <div class="col-sm-10">
            <input class="form-control" id="date" v-model="end" name="date" placeholder="MM/DD/YYY" type="date"/>
          </div>
        </div>

        <fieldset class="form-group">
          <div class="row">
            <legend class="col-form-label col-sm-2 pt-0">Entite(s) Concernee(s):</legend>
            <div class="col-sm-10">
              <div class="form-check-inline">
                <input class="form-check-input" type="checkbox" value="1" id="cmb" v-model="entity">
                <label class="form-check-label" for="CMB">CMB</label>
              </div>
              <div class="form-check-inline">
                <input class="form-check-input" type="checkbox" value="2" id="cmcc" v-model="entity">
                <label class="form-check-label" for="CMCC">CMMC</label>
              </div>
              <div class="form-check-inline">
                <input class="form-check-input" type="checkbox" value="3" id="cmso" v-model="entity">
                <label class="form-check-label" for="CMSO">CMSO</label>
              </div>
              <div class="form-check-inline">
                <input class="form-check-input" type="checkbox" value="4" id="bpe" v-model="entity">
                <label class="form-check-label" for="BPE">BPE</label>
              </div>
              <div class="form-check-inline">
                <input class="form-check-input" type="checkbox" value="5" id="abp" v-model="entity">
                <label class="form-check-label" for="ABP">ABP</label>
              </div>
            </div>
          </div>
        </fieldset>

        <fieldset class="form-group">
          <div class="row">
            <legend class="col-form-label col-sm-2 pt-0">Canaux(s) Concerne(s):</legend>
            <div class="col-sm-10">
              <div class="form-check-inline">
                <input class="form-check-input" type="checkbox" id="defaultCheck1" v-model="canals" value="WEB">
                <label class="form-check-label" for="defaultCheck1">WEB</label>
              </div>
              <div class="form-check-inline">
                <input class="form-check-input" type="checkbox" id="checkgab" v-model="canals" value="GAB">
                <label class="form-check-label" for="checkgab">GAB</label>
              </div>

              <div class="form-check-inline">
                <input class="form-check-input" type="checkbox" value="RWD" id="defaultCheck1" v-model="canals">
                <label class="form-check-label" for="defaultCheck1">RWD</label>
              </div>
            </div>
          </div>
        </fieldset>

        <div id="priorite" style="display: none" class="form-group row">
          <label for="priorite" class="col-sm-2 col-form-label">Priorite GAB:</label>
          <div class="col-sm-10">
            <input type="priorite" class="form-control" id="priorite" v-model="priority">
          </div>
        </div>
        <div class="form-group row" style="display: none" id="cacherbulle">
          <label for="libellem" class="col-sm-2 col-form-label">Libelle Message Conseiller :</label>
          <div class="col-sm-10">
            <p> Votre Conseiller vous Informe </p>
          </div>
        </div>
        <div class="card" style="width: 30rem; background-color : yellow;display:none" >
        <fieldset class="form-group">
          <div class="row"  id="cible">
            <legend class="col-form-label col-sm-2 pt-0"> Cible Client:</legend>
            <div class="col-sm-10">
              <div class="form-check-inline">
                <input class="form-check-input" type="checkbox" id="agence" v-model="agency">
                <label class="form-check-label" for="defaultCheck1">Une CCM/Agence</label>
              </div>
              <div class="form-check-inline">
                <input class="form-check-input" type="checkbox" value="" id="clients" v-model="clientList">
                <label class="form-check-label" for="defaultCheck1">Une Liste de Clients</label>
              </div>
            </div>
          </div>
        </fieldset>
        
        <div class="form-group" style="display:none" id="agencearea">
        <select multiple class="form-control" id="exampleFormControlSelect2">
        <option>Rouen , 123456</option>
        <option>Paris , 457354</option>
        <option>Caen , 998877</option>
        <option>Marseille , 77665544</option>
        <option>Lyon , 443322</option>
        </select>
        </div>

        <div class="form-group" style="display:none" id="listeclient">
        <label for="exampleFormControlFile1">Veuillez deposer la liste</label>
        <input type="file" class="form-control-file" id="exampleFormControlFile1">
        </div>
        </div>
        <div class="form-group row">
          <div class="col-sm-10">
            <button type="submit" class="btn btn-primary">Submit</button>
          </div>
        </div>
      </form>
      </div>
  </div>
  </div>
</template>

 
<script type="text/javascript">
  import axios from 'axios'
  import functions from '@/components/creation/functions.js'
    export default {
    name: 'Creation',
    data () {     
    return {
      //moment = require('moment'),
      info : null,
      libelle: '',
      type: '',
      textMessage : [''],
      motcle: '',
      start : '',
      end : '',
      entity : [''],
      canals : [''], 
      priority: 0,
      agency: true,
      clientList: null,
      target: {
        federation: true,
        agency: true,
        clientList: null,
        targetType: "F",
        villeAgence: ""
      },
    }
  },
  methods: {
      checkForm(e) {
          this.post();
          e.preventDefault();
          this.$goto('/Recherche');
          console.log('After recherche')
      },
      post() {
        axios
          .post('http://localhost:8080/message', {
                type: this.type,
                libelle: this.libelle,
                textMessage : this.textMessage,
                keywords : this.motcles,
                start : this.starting,
                end : this.end,
                entity : this.entity,
                canals : this.canaux , 
                priority: this.priority,
                target : this.target,
          })
          .then(response => (console.log(response.data)))
          .catch(error => console.log(error))
          .finally(() => this.loading = false)
      }
  },
  computed: {
    motcles() {
      return this.motcle.split(",")
    },
    starting() {
      return this.start.toString()
    }
  }
}
</script>


<style>

.hide{
  display: none;
}

#app{
width: 100%;

}
 
.menu{

float: left;
width:25%;
text-align:center;
height: 800px;
background: #212121;
color: #fff;
-webkit-transition: all 0.3s;
-o-transition: all 0.3s;
transition: all 0.3s;
}
#menu .h6 {
    color: #fff; }
  #menu.active {
    margin-left: -250px; }
  #menu h1 {
    margin-bottom: 20px;
    font-weight: 700;
    font-size: 20px; }

.content{
  border:1px solid blue;
/*display:inline;*/
/*padding: 5px;*/
float: right;
width:75%;
text-align:center;
/*position: fixed;*/
}
.barre .barre-ul{
   list-style-type: none;
  margin: 0;
  padding: 0;
  overflow: hidden;
  background-color: #212121;
}
.titleprghp {
background:white;
height: 46px;
color: #000;
font-size: 23px;
height: 46px;
line-height: 40px;
padding: 0 0 0 20px;
margin: 20px 0 20px;
}
ul li {
  cursor: pointer;
  position: relative;
  padding: 12px 8px 12px 40px;
  background:#212121;
  font-size: 18px;
  transition: 0.2s;

  /* make the list items unselectable */
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
</style>