import { useState } from "react";
import { useLocation } from "react-router-dom";
import QueryForm from "../components/QueryForm";
import QuickLinks from "../components/QuickLinks";
import ReportPanel from "../components/ReportPanel";
import StoreMap from "../components/StoreMap";
import { extractErrorMessage, postQuery } from "../api/queryApi";

export default function ChatPage() {
	const location = useLocation();
	const [loading, setLoading] = useState(false);
	const [error, setError] = useState(null);
	const [result, setResult] = useState(null);

	async function handleSubmit(message) {
		setLoading(true);
		setError(null);
		try {
			const data = await postQuery(message);
			setResult(data);
		} catch (err) {
			setError(extractErrorMessage(err));
			setResult(null);
		} finally {
			setLoading(false);
		}
	}

	return (
		<div className="page">
			<div className="page-intro">
				<span className="page-kicker">AI 창업 상담사</span>
				<h1>AI 창업 상담</h1>
				<p>자연어로 물어보면, 조회된 상권 데이터를 근거로 AI가 입지를 해석해드립니다.</p>
			</div>

			<QuickLinks />

			<div id="query-form">
				<QueryForm onSubmit={handleSubmit} loading={loading} initialMessage={location.state?.prefill} />
			</div>

			{error && <div className="error-box">{error}</div>}

			{result && (
				<div className="result-grid">
					<ReportPanel result={result} />
					<StoreMap stats={result.stats} />
				</div>
			)}
		</div>
	);
}
