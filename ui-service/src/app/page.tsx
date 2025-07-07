import Image from "next/image";
import login from "@/app/auth/login/page";
import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";
import styles from "./components/page.module.css";

export default function Home() {
  return (
    <div className="">
      <Navbar />
      <Sidebar />
    </div>
  );
}
