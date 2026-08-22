import { useState } from "react";

const EXAMPLES = [
	"둔산동에서 카페 하나 차리려는데 괜찮을까?",
	"노은동에 치킨집 내면 경쟁이 심할까?",
	"나성동 편의점 자리 어때?",
];

export default function QueryForm({ onSubmit, loading, initialMessage }) {
	const [message, setMessage] = useState(initialMessage ?? "");

	function handleSubmit(e) {
		e.preventDefault();
		if (!message.trim() || loading) return;
		onSubmit(message.trim());
	}

	return (
		<form className="query-form" onSubmit={handleSubmit}>
			<p className="query-form-label">상권 질문을 입력하세요</p>
			<div className="query-input-row">
				<textarea
					className="query-input"
					placeholder="예: 둔산동에서 카페 하나 차리려는데 괜찮을까?"
					value={message}
					onChange={(e) => setMessage(e.target.value)}
					rows={1}
				/>
				<button className="submit-button" type="submit" disabled={loading || !message.trim()}>
					{loading ? "분석 중..." : "분석"}
				</button>
			</div>
			<div className="query-examples">
				{EXAMPLES.map((example) => (
					<button
						type="button"
						key={example}
						className="example-chip"
						onClick={() => setMessage(example)}
					>
						{example}
					</button>
				))}
			</div>
		</form>
	);
}
