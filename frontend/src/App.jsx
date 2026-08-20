import { BrowserRouter, Route, Routes } from "react-router-dom";
import "leaflet/dist/leaflet.css";
import Layout from "./components/Layout";
import ChatPage from "./pages/ChatPage";
import ExplorePage from "./pages/ExplorePage";
import ComparePage from "./pages/ComparePage";
import FavoritesPage from "./pages/FavoritesPage";
import "./App.css";

export default function App() {
	return (
		<BrowserRouter>
			<Routes>
				<Route element={<Layout />}>
					<Route path="/" element={<ChatPage />} />
					<Route path="/explore" element={<ExplorePage />} />
					<Route path="/compare" element={<ComparePage />} />
					<Route path="/favorites" element={<FavoritesPage />} />
				</Route>
			</Routes>
		</BrowserRouter>
	);
}
