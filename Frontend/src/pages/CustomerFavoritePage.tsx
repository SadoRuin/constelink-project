import styles from "./CustomerFavoritePage.module.css"
import { useState, useEffect } from 'react';
import axios from "axios";
import DonationCard from './../components/cards/DonationCard';
const CustomerFavoritePage = () => {
    const [bookedList, setBookedList] = useState([]);
    
    useEffect(()=>{
        let params:any={page:1,size:8,memberId:1}
        axios.get("/bookmarks",{params}).then(res=>{
            console.log(res.data.content);
            setBookedList(res.data.content)
        }).catch((err) => console.log(err.response.data)
        )
    },[])



    
    
    return (
        <div className={styles.CustomerFavoritePage}>
            <div className={styles.fav_title}>관심 모금 목록</div>
            <div className={styles.fav_box}>
                <ul className={styles.fav_list}>
                    {bookedList.map((it,idx)=>{
                        console.log(it);
                    return <li className={styles.fav_item} key={idx} ><DonationCard   data={it} /></li>
                    })}
                </ul>
            </div>
            

        </div>
    );
};

export default CustomerFavoritePage;