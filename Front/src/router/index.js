import Vue from 'vue'
import Router from 'vue-router'
import Test from '@/components/Test'
import Creation from '@/components/creation/Creation'
import Recherche from '@/components/recherche/Recherche'
import Modification from '@/components/modification/Modification'


Vue.use(Router)

export default new Router({
  routes: [
    {
    	path: '/',
    	name: 'Test',
    	component: Test
    },
    {
    	path: '/creation',
    	name: 'Creation',
    	component: Creation
    },
    {
        path: '/recherche',
        name: 'Recherche',
        component: Recherche
    },
    {
        path: '/modification',
        name: 'Modification',
        component: Modification
    }
  ]
})
