import { Route, Routes } from "react-router-dom"
import './App.css';
import HomePage from './pages/HomePage';
import Login from './pages/Login';
import MainLayout from "./components/MainLayout";
// import Header from './components/header/Header';
// import Login from './pages/Login';

// 병원 페이지
import HospitalPage from "./pages/HospitalPage";
import BenRegister from "./pages/BenRegister";
import BenEdit from "./pages/BenEdit";
import FundRegister from "./pages/FundRegister";
import HosBenList from "./pages/HosBenList";
import HosFundList from "./pages/HosFundList";
import NoticePage from './pages/NoticePage';
import CustomerMyHome from './pages/CustomerMyHome';

import RecoveryDiary from './pages/RecoveryDiary';
import RecoveryDiaryDetail from './pages/RecoveryDiaryDetail';

function App() {
  return (
      <div className="App">
       
        <Routes>
          <Route element={<MainLayout/>}>
            <Route path='/' element={<HomePage/>}/>
            <Route path='/hospage' element={<HospitalPage />}/>
            <Route path='/benregi' element={<BenRegister />}/>
            <Route path='/benedit' element={<BenEdit />}/>
            <Route path='/fundregi' element={<FundRegister />}/>
            <Route path='/hosbenlist' element={<HosBenList />}/>
            <Route path='/hosfundlist' element={<HosFundList />}/>
            <Route path="/notice/*" element={<NoticePage/>}/>
            <Route path="/mypage/*" element={<CustomerMyHome/>}/>
            <Route path='/diary' element={<RecoveryDiary/>}/>
            <Route path='/diarydetail/:id' element={<RecoveryDiaryDetail/>}/>
          </Route >
         
           <Route path='/login' element={<Login />}/>
        </Routes>
      </div> 
  )}
export default App;

